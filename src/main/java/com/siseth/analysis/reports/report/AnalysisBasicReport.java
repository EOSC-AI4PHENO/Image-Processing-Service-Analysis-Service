package com.siseth.analysis.reports.report;

import com.siseth.analysis.entity.AnalysisParameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalysisBasicReport extends AnalysisReportAbstract{

    public AnalysisBasicReport(Long analysisId, String sort) {
        super(analysisId, LocalDate.now(), LocalDate.now(), false, sort);
    }

    public String process(String result) {
        this.result = result;
        this.sql = this.sql.replace("__analysisId__", this.analysisId.toString())
                .replaceAll("__ids__", this.parameterIds)
                .replace("__parameters__", this.result);
        this.sql = this.sql + getOrder();
        return this.sql;
    }
    public List<Map<String,Object>> getData(List<AnalysisParameter> parameters, JdbcTemplate jdbcTemplate) {
        setParameters(parameters);
        this.result = jdbcTemplate.queryForObject(getParameterSQL(), String.class);
        return jdbcTemplate.queryForList(process(this.result));
    }


    private String sql =
            "select * from crosstab (" +
                    "   'SELECT     " +
                    "                dense_rank() OVER (ORDER BY ad.\"analysisId\", ad.\"imageId\")::int AS row_name,  " +
                    "                ad.\"analysisId\", " +
                    "                to_char(\"imageCreatedAt\" , ''YYYY-MM-DDThh24:MI:SS'') AS \"imageCreatedAt\", " +
                    "                ad.\"imageId\" AS \"imageId\", " +
                    "                ad.\"roiId\" AS \"roiId\", " +
                    "                ad.\"roiResultId\" AS \"roiResultId\", " +
                    "                ar.\"parameterId\", " +
                    "                MAX(ar.value) AS value " +
                    "            FROM public.\"AnalysisResult\" ar " +
                    "                INNER JOIN public.\"AnalysisDetail\" ad " +
                    "                   ON ar.\"detailsId\"  = ad.id " +
                    "            WHERE " +
                    "                ad.\"analysisId\"  = __analysisId__ AND " +
                    "                ar.\"isActive\" = TRUE AND ad.\"isActive\" = TRUE AND " +
                    "                ar.\"parameterId\" IN (__ids__) "  +
                    "            GROUP BY " +
                    "                ad.\"analysisId\", ar.\"parameterId\", ad.\"imageCreatedAt\", ad.\"roiId\", ad.\"roiResultId\", ad.\"imageId\"', " +
                    "           'select id from public.\"AnalysisParameter\" WHERE id in (__ids__) ORDER BY COALESCE(\"orderNumber\",0)' " +
                    "                " +
                    ") AS " +
                    "  newTable(__row_name integer, __analysisId bigint, date text,imageId bigint, roiId bigint, roiResultId bigint , __parameters__ " +
                    ") ";

}
