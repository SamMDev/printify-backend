package com.example.printifybackend.jdbi;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * Model used for filtering and loading
 */
@Getter @Setter
public class Criteria {
    private int offset;
    private int limit;
    private LinkedHashMap<String, Object> filters;
}
