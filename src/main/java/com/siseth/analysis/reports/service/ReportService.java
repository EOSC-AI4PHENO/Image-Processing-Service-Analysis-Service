package com.siseth.analysis.reports.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.analysis.component.entity.BaseEntity;
import com.siseth.analysis.component.file.CSVCreator;
import com.siseth.analysis.component.file.ExcelCreator;
import com.siseth.analysis.dto.analysis.request.respond.AnalysisParameterResDTO;
import com.siseth.analysis.dto.analysis.request.respond.RawDataResDTO;
import com.siseth.analysis.dto.analysis.request.respond.SourcePreVerificationResDTO;
import com.siseth.analysis.dto.analysis.request.respond._RawDataResDTO;
import com.siseth.analysis.dto.report.request.ReportAttributeReqDTO;
import com.siseth.analysis.entity.*;
import com.siseth.analysis.feign.dataSource.DataSourceAdapterServiceImpl;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import com.siseth.analysis.feign.fedora.FedoraService;
import com.siseth.analysis.feign.fedora.FedoraServiceImpl;
import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import com.siseth.analysis.reports.constant.ReportType;
import com.siseth.analysis.reports.report.*;
import com.siseth.analysis.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.siseth.analysis.reports.constant.ReportType.*;
import static org.jfree.chart.ChartUtilities.writeChartAsPNG;

@Service
@RequiredArgsConstructor
public class ReportService {


    private final AnalysisRepository analysisRepository;
    private final AnalysisDetailRepository analysisDetailRepository;
    private final AnalysisParameterRepository analysisParameterRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final SourcePreVerificationRepository sourcePreVerificationRepository;
    private final FedoraServiceImpl fedoraFeign;

    private final DataSourceAdapterServiceImpl dataSourceAdapterService;

    private final PatternRepository patternRepository;
    private Map<String, Object> paramResults;
    private List<Map<String, Object>> results;

    private final JdbcTemplate jdbcTemplate;

    public File getReportCSV(ReportAttributeReqDTO api) {
        List<Map<String, Object>> data = getReport(api);
        return new CSVCreator(data).createFile();
    }

    public File getReportExcel(ReportAttributeReqDTO api) {
        List<Map<String, Object>> data = getReport(api);
        return new ExcelCreator(data).createFile();
    }

    public File getAnalysisExcelExtend(Long analysisId) {
        Analysis analysis = analysisRepository.findById(analysisId)
                                        .orElseThrow(() -> new RuntimeException("Not found!"));

        ImageSourceShortResDTO source = dataSourceAdapterService.getSource(analysis.getSourceId());
        Map<String, Object> map = Optional.ofNullable(source)
                                                .map(ImageSourceShortResDTO::getMap)
                                                .orElse(new LinkedHashMap<>());
        map.put("Algorithm Name", analysis.getAlgorithmName());
        map.put("Description", analysis.getDesc());
        List<Map<String, Object>> data = getReport(new ReportAttributeReqDTO(ANALYSIS_BASIC, analysisId));

        return new ExcelCreator(map, data).createFile();
    }

    public File getAnalysisCSVExtend(Long analysisId) {
        List<Map<String, Object>> data = getReport(new ReportAttributeReqDTO(ANALYSIS_EXTEND, analysisId));

        data.forEach(x -> {
            FileShortResDTO dto =  fedoraFeign.getFilesImageDataById(Long.valueOf(x.get("imageid").toString()));
            if(dto != null) {
                x.replace("photo_name", dto.getName());
                x.replace("directory", dto.getDirectory());
            }
            });
        return new CSVCreator(data).createFile();
    }

    public List<Map<String, Object>> getReport(ReportAttributeReqDTO api) {

        List<AnalysisParameter> parameters = null;
        switch (api.getType()) {
            case ANALYSIS_BASIC:
                AnalysisBasicReport basicReport = new AnalysisBasicReport(api.getAnalysisId(), api.getSort());
                parameters = analysisParameterRepository.getAllToAnalysis(api.getAnalysisId());
                return basicReport.getData(parameters, jdbcTemplate);
            case ANALYSIS_EXTEND:
                AnalysisExtendReport extendReport = new AnalysisExtendReport(api.getAnalysisId(), api.getSort());
                parameters = analysisParameterRepository.findAllByValue("REPORT_DATA");
                return extendReport.getData(parameters, jdbcTemplate);
            case ANALYSIS_MEDIAN:
                GetMedianReport medianReport = new GetMedianReport(api.getAnalysisId(),
                                                            api.getDateFrom(),
                                                            api.getDateTo(),
                                                            api.getSort());
                parameters = analysisParameterRepository.findAllByNameIn(medianReport.getParameterNames());
                return medianReport.getData(parameters, jdbcTemplate);
            case ANALYSIS_90_PERCENTILE:
                Get90PercentileReport percentileReport = new Get90PercentileReport(api.getAnalysisId(),
                                                                            api.getDateFrom(),
                                                                            api.getDateTo(),
                                                                            api.getSort());
                parameters = analysisParameterRepository.findAllByNameIn(percentileReport.getParameterNames());
                return percentileReport.getData(parameters, jdbcTemplate);

            case ANALYSIS_FROM_FORMULA:
                Pattern pattern = patternRepository.findById(api.getPatternId()).orElseThrow( () -> new RuntimeException("Not found!"));
                GetFormulaReport formulaReport = new GetFormulaReport(api.getAnalysisId(),
                        api.getDateFrom(),
                        api.getDateTo(),
                        api.getSort(),
                        pattern);
                parameters = analysisParameterRepository.findAllByNameIn(formulaReport.getParameterNames());
                List<Map<String, Object>> results = formulaReport.getData(parameters, jdbcTemplate);

                Set<String> parameterNames= parameters.stream().map(AnalysisParameter::getName).collect(Collectors.toSet());
                results.forEach(result -> result.put("expressionResult", calculate(pattern.getFormula(),result, parameterNames)));
                return results;

        }

        throw new RuntimeException("Wrong report type");

    }

    private BigDecimal calculate(String pattern, Map<String, Object> map, Set<String> parameterNames) {



        Expression expression = new ExpressionBuilder(pattern).variables(parameterNames).build();

        for (String parameter : parameterNames) {
            expression.setVariable(parameter, Double.parseDouble(map.get(parameter).toString()));
        }
        return BigDecimal.valueOf(expression.evaluate());

    }
    @SneakyThrows
    public _RawDataResDTO _getRawData(Long analysisId, boolean addPreviewPhoto, String sortString) {
        List<Map<String, Object>> data = getReport(new ReportAttributeReqDTO(ANALYSIS_BASIC, analysisId, sortString));

        String previewPhotoInBase = addPreviewPhoto && data.size() > 0 ?
                fedoraFeign.getPreviewPhotoForImageId((Long) data.get(0).get("imageid")) :
                "";

        return new _RawDataResDTO(data, previewPhotoInBase);
    }

    public RawDataResDTO getRawData(Long analysisId, boolean addPreviewPhoto, int pageNumber, int pageSize, String sortString) {
        String previewPhotoInBase = "";
        Sort sort = getSortForRawData(sortString);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<AnalysisDetail> allByAnalysisIdAndStatus = analysisDetailRepository.findAllByAnalysisIdAndStatus(analysisId, AnalysisDetail.AnalysisStatus.FINISHED, pageable);
        Page<Map<String, Object>> resultsPage = allByAnalysisIdAndStatus
                .map(detail -> {
                    Map<String, Object> paramResults = new HashMap<>();
                    paramResults.put("imageId", detail.getImageId());
                    paramResults.put("roiId", detail.getRoiId());
                    paramResults.put("roiResultId", detail.getRoiResultId());
                    paramResults.put("date", detail.getImageCreatedAt());

                    List<AnalysisResult> analysisResults = analysisResultRepository.findAllByDetail(detail)
                            .orElseThrow(() -> new EntityNotFoundException("Results not found"));
                    analysisResults.forEach(result -> paramResults.put(result.getParamName(), result.getValue()));
                    paramResults.remove("isFloweringConfidence");
                    return paramResults;
                });

        if(addPreviewPhoto && !allByAnalysisIdAndStatus.getContent().isEmpty() && !resultsPage.getContent().isEmpty()) {
            Long previewImageId = allByAnalysisIdAndStatus.getContent().get(0).getImageId();
            previewPhotoInBase = fedoraFeign.getPreviewPhotoForImageId(previewImageId);
        }

        return new RawDataResDTO(resultsPage, previewPhotoInBase);
    }

    private Sort getSortForRawData(String sortString) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (sortString != null && sortString.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
            return Sort.by(direction, "id");
    }

    public List<Map<String, Object>> getRawData(Long analysisId) {
        paramResults = new HashMap<>();
        results = new ArrayList<>();

        List<AnalysisDetail> analysisDetails = analysisDetailRepository
                        .findAllByAnalysisIdAndStatus(analysisId, AnalysisDetail.AnalysisStatus.FINISHED)
                        .orElseThrow( () -> new EntityNotFoundException("Details not found"));

        analysisDetails.forEach(detail -> {
            paramResults.put("imageId", detail.getImageId());
            paramResults.put("roiId", detail.getRoiId());
            paramResults.put("roiResultId", detail.getRoiResultId());
            paramResults.put("date", detail.getImageCreatedAt());
            List<AnalysisResult> analysisResults = analysisResultRepository.findAllByDetail(detail)
                            .orElseThrow( () -> new EntityNotFoundException("Results not found"));
            analysisResults.forEach(result -> paramResults.put(result.getParamName(), result.getValue()));
            paramResults.remove("isFloweringConfidence");
            results.add(new HashMap<>(paramResults));
        });

        return results;
    }

    public List<Map<String, Object>> getRawDataInTimeRange(Long analysisId, LocalDateTime startTime, LocalDateTime endTime) {
        paramResults = new HashMap<>();
        results = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        if (startTime == null) {
            startTime = LocalDateTime.of(0, 1, 1, 0, 0);
        }

        if (endTime == null) {
            endTime = now;
        }

        List<AnalysisDetail> analysisDetails = analysisDetailRepository
                .findAllByAnalysisIdAndStatusAndImageCreatedAtBetween(analysisId, AnalysisDetail.AnalysisStatus.FINISHED, startTime, endTime)
                .orElseThrow( () -> new EntityNotFoundException("Details not found"));

        analysisDetails.forEach(detail -> {
            paramResults.put("imageId", detail.getImageId());
            paramResults.put("roiId", detail.getRoiId());
            paramResults.put("roiResultId", detail.getRoiId());
            paramResults.put("date", detail.getImageCreatedAt());
            List<AnalysisResult> analysisResults = analysisResultRepository.findAllByDetail(detail)
                    .orElseThrow( () -> new EntityNotFoundException("Results not found"));
            analysisResults.forEach(result -> paramResults.put(result.getParamName(), result.getValue()));
            paramResults.remove("isFloweringConfidence");
            results.add(new HashMap<>(paramResults));
        });

        results.sort(Comparator.comparing(map -> ((LocalDateTime) map.get("date"))));

        return results;
    }
    @SneakyThrows
    public byte[] getColorContribution(Long analysisId) {
        List<Map<String, Object>> params = getRawData(analysisId);


        // Przykład dla prostego wykresu słupkowego
        XYSeries series1 = new XYSeries("RED");
        AtomicInteger iter = new AtomicInteger();
        params.forEach(x -> {
            series1.add(iter.getAndIncrement(), (BigDecimal)x.get("r_av"));
            System.out.println(x.get("isFlowering"));
        });

        XYSeries series2 = new XYSeries("BLUE");
        iter.set(0);
        params.forEach(x -> {
            series2.add(iter.getAndIncrement(), (BigDecimal)x.get("b_av"));
        });


        XYSeries series3 = new XYSeries("GREEN");
        iter.set(0);
        params.forEach(x -> {
            series3.add(iter.getAndIncrement(), (BigDecimal)x.get("g_av"));
        });

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Color contribution chart",  // tytuł wykresu
                "Day",          // oś X
                "percentage contribution",          // oś Y
                dataset,         // zestaw danych
                PlotOrientation.VERTICAL,  // orientacja wykresu
                true,            // legend
                true,            // tooltips
                false            // urls
        );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeChartAsPNG(byteArrayOutputStream, chart, 500, 500);

        return byteArrayOutputStream.toByteArray();
    }

    public List<Map<String, Object>> getRawDataByImageId(Long imageId) {
        paramResults = new HashMap<>();
        results = new ArrayList<>();

        List<AnalysisDetail> analysisDetails = analysisDetailRepository
                .findAllByImageIdAndStatus(imageId, AnalysisDetail.AnalysisStatus.FINISHED)
                .orElseThrow( () -> new EntityNotFoundException("Details not found"));

        analysisDetails.forEach(detail -> {
            paramResults.put("imageId", detail.getImageId());
            paramResults.put("roiId", detail.getRoiId());
            List<AnalysisResult> analysisResults = analysisResultRepository.findAllByDetail(detail)
                    .orElseThrow( () -> new EntityNotFoundException("Results not found"));
            analysisResults.forEach(result -> paramResults.put(result.getParamName(), result.getValue()));
            paramResults.remove("isFloweringConfidence");
            results.add(new HashMap<>(paramResults));
        });

        return results;
    }

    public SourcePreVerificationResDTO getSourcePreVerByImageId(Long imageId) {
       SourcePreVerification sourcePreVerification = sourcePreVerificationRepository.findByImageId(imageId).orElseGet(SourcePreVerification::new);
       return new SourcePreVerificationResDTO(sourcePreVerification);
    }

    public List<AnalysisParameterResDTO> getParameters() {
        return analysisParameterRepository.findAllByValue("REPORT_DATA").stream().map(AnalysisParameterResDTO::new).collect(Collectors.toList());
    }
}
