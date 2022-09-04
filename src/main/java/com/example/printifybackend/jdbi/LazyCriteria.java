package com.example.printifybackend.jdbi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Model used for filtering and loading
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LazyCriteria {
    private Long offset;
    private Long limit;
    private Map<String, Object> filter;
}
