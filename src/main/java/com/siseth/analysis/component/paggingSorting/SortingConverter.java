package com.siseth.analysis.component.paggingSorting;

import org.springframework.data.domain.Sort;

public final class SortingConverter {

    public static Sort convert(String sortingString){
        Sort sort = Sort.unsorted();
        if(sortingString != null && !sortingString.equals("")) {
            String[] sorts = sortingString.split(",");
            for (String s : sorts) {
                SortDTO sortDTO = new SortDTO(s);
                Sort sorting = sortDTO.getSort();
                sort = sort.and(sorting);
            }
        } else {
            sort = sort.and(Sort.by("id").ascending());
        }
        return sort;
    }

}

