package com.example.printifybackend.jdbi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    private String result;

    protected DaoFilterQueryBuilder(String select) {
        this.select = select;
        this.bind = new HashMap<>();
    }

    /**
     * Builds where statement and the result query will have this statement
     * Call this if you want the query to have where statement part
     *
     * Building where statement should be implemented separately in every instance,
     * because it always differs
     *
     * @param filters   filters to build where query from
     */
    public abstract void buildAndAddWhereStatement(Map<String, Object> filters);

    /**
     * Builds limit offset statement and the result query will have this statement
     * Call this if you want the query to have limit offset part
     */
    public void buildAndAddLimitOffsetStatement(Long limit, Long offset) {
        final StringBuilder limitOffsetBuilder = new StringBuilder();
        if (limit != null) limitOffsetBuilder.append(String.format("LIMIT %d", limit));
        if (offset != null) limitOffsetBuilder.append(String.format(" OFFSET %d", offset));
        this.limitOffsetStatement = limitOffsetBuilder.toString();
    }

    public void buildAndAddOrderByStatement(OrderType type, String... orderFields) {
        if (orderFields == null || orderFields.length == 0) return;

        final StringBuilder orderByBuilder = new StringBuilder();
        orderByBuilder.append("ORDER BY ").append(String.join(",", orderFields));
        if (type != null) orderByBuilder.append(" ").append(type.getValue());

        this.orderStatement = orderByBuilder.toString();
    }

    public String build() {
        if (StringUtils.isNotBlank(this.result)) return this.result;

        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(this.select);
        if (StringUtils.isNotBlank(this.whereStatement)) queryBuilder.append(" ").append(this.whereStatement);
        if (StringUtils.isNotBlank(this.orderStatement)) queryBuilder.append(" ").append(this.orderStatement);
        if (StringUtils.isNotBlank(this.limitOffsetStatement)) queryBuilder.append(" ").append(this.limitOffsetStatement);

        this.result = queryBuilder.toString();
        return this.result;
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

    @RequiredArgsConstructor @Getter
    public enum OrderType {
        ASC("ASC"),
        DESC("DESC");

        private final String value;
    }
}
