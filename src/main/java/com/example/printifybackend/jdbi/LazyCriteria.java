package com.example.printifybackend.jdbi;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * Model used for filtering and loading
 */
@Getter @Setter
@Data
public class LazyCriteria {
    private Long offset;
    private Long limit;
    private LinkedHashMap<String, Object> filter;

    public Long getOffset() {
        return this.offset != null ? this.offset : 0;
    }

    public Long getLimit() {
        return this.limit != null ? this.limit : 0;
    }
}
