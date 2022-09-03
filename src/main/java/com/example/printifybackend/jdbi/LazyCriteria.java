package com.example.printifybackend.jdbi;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * Model used for filtering and loading
 */
@Getter @Setter
public class LazyCriteria {
    private Long offset;
    private Long limit;
    private LinkedHashMap<String, Object> filter;
}
