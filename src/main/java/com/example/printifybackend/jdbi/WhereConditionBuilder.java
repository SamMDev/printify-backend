package com.example.printifybackend.jdbi;

import java.util.ArrayList;
import java.util.List;

/**
 * To build where condition
 */
public class WhereConditionBuilder {
    private final List<String> conditions = new ArrayList<>();

    /**
     * Adds new condition to where statement
     * @param condition     new condition
     */
    public void addCondition(String condition) {
        this.conditions.add(condition);
    }

    /**
     * Builds the condition
     * @return      where condition
     */
    public String buildWhere() {
        return this.conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
    }
}
