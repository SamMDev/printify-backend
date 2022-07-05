package com.example.printifybackend.jdbi;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for query
 * Should be used for all the complex queries that contain filter map to build where statement
 *
 * @author Samuel Molcan
 */
public abstract class DaoFilterQueryBuilder {

    @Getter
    private final Map<String, Object> bind;

    @Getter
    private final String select;

    @Getter @Setter
    private String whereStatement;

    @Getter @Setter
    private String orderStatement;

    @Getter
    private String limitOffsetStatement;

    protected DaoFilterQueryBuilder(String select) {
        this.select = select;
        this.bind = new HashMap<>();
    }

    /**
     * Building where statement should be implemented separately in every instance,
     * because it always differs
     *
     * @param filters   filters to build where query from
     */
    public abstract void buildWhereStatement(Map<String, Object> filters);

    public void buildLimitOffsetStatement(Long limit, Long offset) {
        this.limitOffsetStatement = String.format("LIMIT %d OFFSET %d", limit, offset);
    }

    public String build() {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(this.select);
        if (StringUtils.isNotBlank(this.whereStatement)) queryBuilder.append(" ").append(this.whereStatement);
        if (StringUtils.isNotBlank(this.orderStatement)) queryBuilder.append(" ").append(this.orderStatement);
        if (StringUtils.isNotBlank(this.limitOffsetStatement)) queryBuilder.append(" ").append(this.limitOffsetStatement);
        return queryBuilder.toString();
    }

    /**
     * To build where condition
     */
    public static class WhereConditionBuilder {
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
}
