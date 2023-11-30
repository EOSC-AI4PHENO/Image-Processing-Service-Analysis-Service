package com.siseth.analysis.reports.service;

import com.siseth.analysis.dto.analysis.request.respond.AnalysisParameterResDTO;
import com.siseth.analysis.dto.analysis.request.respond.RawDataResDTO;
import com.siseth.analysis.dto.analysis.request.respond.SourcePreVerificationResDTO;
import com.siseth.analysis.dto.report.request.ReportAttributeReqDTO;
import com.siseth.analysis.entity.*;
import com.siseth.analysis.feign.fedora.FedoraService;
import com.siseth.analysis.reports.report.Get90PercentileReport;
import com.siseth.analysis.reports.report.GetFormulaReport;
import com.siseth.analysis.reports.report.GetMedianReport;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.jfree.chart.ChartUtilities.writeChartAsPNG;

@Service
@RequiredArgsConstructor
public class ReportInternalService {

    private final AnalysisParameterRepository analysisParameterRepository;

    public List<AnalysisParameterResDTO> getParameters() {
        return analysisParameterRepository.findAllByOrderByOrderNumber()
                .stream()
                .map(AnalysisParameterResDTO::new)
                .collect(Collectors.toList());
    }
}
