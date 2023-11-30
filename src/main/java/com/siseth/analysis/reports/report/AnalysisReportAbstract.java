package com.siseth.analysis.reports.report;

import com.siseth.analysis.entity.AnalysisParameter;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AnalysisReportAbstract {

    protected Long analysisId;

    protected String result;

    protected String dateFrom;

    protected String dateTo;

    protected String parameterIds;

    private String sort;

    private Boolean orderById;



    public AnalysisReportAbstract(Long analysisId,
                                  LocalDate dateFrom, LocalDate dateTo,
                                  Boolean orderById, String sort) {
        this.analysisId = analysisId;
        this.dateFrom = dateFrom.toString();
        this.dateTo = dateTo.toString();
        this.orderById = orderById;
        this.sort = sort;
    }


    public void setParameters(List<AnalysisParameter> parameters) {
        this.parameterIds = parameters
                .stream()
                .map(x -> x.getId().toString())
                .collect(Collectors.joining(","));
        this.parameterSQL = this.orderById ?
                this.parameterSQL.replace("__ids__", this.parameterIds) :
                this.parameterSQL_Order.replace("__ids__", this.parameterIds);
    }

    protected String getOrder() {
        return this.sort != null && !this.sort.equals("")?
                    "ORDER BY " + this.sort :
                    "";
    }


    @Getter
    private String parameterSQL = "SELECT string_agg('\"'||(name)||'\" NUMERIC(14,6) ', ',' ORDER BY id) " +
            " FROM public.\"AnalysisParameter\" " +
            " WHERE id in (__ids__);";

    private String parameterSQL_Order = "SELECT string_agg('\"'||(name)||'\" NUMERIC(14,6) ', ',' ORDER BY COALESCE(\"orderNumber\", 0)) " +
            " FROM public.\"AnalysisParameter\" " +
            " WHERE id in (__ids__);";
}
