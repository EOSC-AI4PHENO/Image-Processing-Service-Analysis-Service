package com.siseth.analysis.reports.report;

import com.siseth.analysis.entity.AnalysisParameter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalysisExtendReport  extends AnalysisReportAbstract{

    public AnalysisExtendReport(Long analysisId, String sort) {
        super(analysisId, LocalDate.now(), LocalDate.now(), true, sort);
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
            "select * from crosstab ( \n" +
                    "   'SELECT     \n" +
                    "                dense_rank() OVER (ORDER BY ad.\"analysisId\", ad.\"imageId\")::int AS row_name,  \n" +
                    "                ad.\"analysisId\",  \n" +
                    "                ad.\"imageId\" AS \"imageId\", \n" +
                    "                '''' AS photo_name,  \n" +
                    "                '''' AS directory,  \n" +
                    "                to_char(\"imageCreatedAt\" , ''YYYY-MM-DDThh24:MI:SS'') AS \"imageCreatedAt\",  \n" +
                    "                ad.\"roiId\" AS \"roiId\",  \n" +
                    "                ar.\"parameterId\",  \n" +
                    "                MAX(ar.value) AS value  \n" +
                    "            FROM public.\"AnalysisResult\" ar  \n" +
                    "                INNER JOIN public.\"AnalysisDetail\" ad  \n" +
                    "                   ON ar.\"detailsId\"  = ad.id  \n" +
                    "            WHERE  \n" +
                    "                ad.\"analysisId\"  = __analysisId__ AND  \n" +
                    "                ar.\"isActive\" = TRUE AND ad.\"isActive\" = TRUE AND  \n" +
                    "                ar.\"parameterId\" IN (__ids__)  \n" +
                    "            GROUP BY  \n" +
                    "                ad.\"analysisId\", ar.\"parameterId\", ad.\"imageCreatedAt\", ad.\"roiId\", ad.\"imageId\"',  \n" +
                    "           'select id from public.\"AnalysisParameter\" WHERE id in (__ids__) ORDER BY id'  \n" +
                    "                 \n" +
                    ") AS  \n" +
                    "  newTable(row_name integer, analysisId bigint, imageId bigint, photo_name text, directory text, date text, roiId bigint , __parameters__  \n" +
                    ") ";


}
