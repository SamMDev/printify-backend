package com.example.printifybackend.jdbi;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class LazyDataModel {
    private Long totalRowsCount;
    private Long offset;
    private Long end;
    private List<?> data;
}
