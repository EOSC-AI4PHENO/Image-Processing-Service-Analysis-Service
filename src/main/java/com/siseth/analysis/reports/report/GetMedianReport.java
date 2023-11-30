package com.siseth.analysis.reports.report;

import com.siseth.analysis.entity.AnalysisParameter;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetMedianReport extends AnalysisReportAbstract{

    @Getter
    public List<String> parameterNames = List.of("r_av","g_av","b_av", "bri_av", "gi_av", "bi_av", "ri_av","number_of_lindens","isFlowering");

    public GetMedianReport(Long analysisId, LocalDate dateFrom, LocalDate dateTo, String sort) {
        super(analysisId, dateFrom, dateTo, true,  sort);
    }

    public List<Map<String,Object>> getData(List<AnalysisParameter> parameters, JdbcTemplate jdbcTemplate) {
        setParameters(parameters);
        this.result = jdbcTemplate.queryForObject(getParameterSQL(), String.class);
        return jdbcTemplate.queryForList(process(this.result));
    }

    public String process(String result) {
        this.result = result;
        this.sql = this.sql.replace("__analysisId__", this.analysisId.toString())
                .replaceAll("__ids__", this.parameterIds)
                .replace("__dateFrom__", this.dateFrom)
                .replace("__dateTo__", this.dateTo)
                .replace("__parameters__", this.result);
        this.sql = this.sql + getOrder();
        return this.sql;
    }

    String sql =
            "select * from crosstab (" +
                    "   'SELECT     " +
                    "                dense_rank() OVER (ORDER BY ad.\"analysisId\", CAST(ad.\"imageCreatedAt\" AS DATE))::int AS row_name," +
                    "                ad.\"analysisId\", " +
                    "                CAST(ad.\"imageCreatedAt\" AS DATE) AS \"date\", " +
                    "                ar.\"parameterId\", " +
                    "                PERCENTILE_CONT(0.5) WITHIN GROUP(ORDER BY ar.value) AS value " +
                    "            FROM  " +
                    "                public.\"AnalysisResult\" ar " +
                    "                INNER JOIN public.\"AnalysisDetail\" ad ON ar.\"detailsId\"  = ad.id " +
                    "            WHERE " +
                    "                ''__dateFrom__'' <= CAST(ad.\"imageCreatedAt\" AS DATE) AND " +
                    "                ''__dateTo__'' >= CAST(ad.\"imageCreatedAt\" AS DATE) AND " +
                    "                ad.\"analysisId\"  = __analysisId__ AND " +
                    "                ar.\"isActive\" = TRUE AND ad.\"isActive\" = TRUE AND " +
                    "                ar.\"parameterId\" IN (__ids__) "  +
                    "            GROUP BY " +
                    "                ad.\"analysisId\", ar.\"parameterId\", CAST(ad.\"imageCreatedAt\" AS DATE)', " +
                    "           'select id from public.\"AnalysisParameter\" WHERE id in (__ids__) ORDER BY id' " +
                    "                " +
                    ") AS " +
                    "  newTable(row_name integer, analysisId bigint, \"date\" date, __parameters__ " +
                    ") ";

}
