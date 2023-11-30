package com.siseth.analysis.component.paggingSorting;

import org.springframework.data.domain.Sort;

public class SortDTO {

    private String sortBy;

    private Sort.Direction sortingDirection;

    public SortDTO(String sort) {
        String[] sortSplit = sort.split(":");
        this.sortBy = sortSplit[0];
        this.sortingDirection = Sort.Direction.valueOf(sortSplit[1]);
    }

    public Sort getSort(){
        Sort sort;
        sort = Sort.by(new Sort.Order(sortingDirection, sortBy));
        return sort;
    }
}
