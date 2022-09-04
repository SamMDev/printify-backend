package com.example.printifybackend.order;

import com.example.printifybackend.contact_into.EntityContactInfo;
import com.example.printifybackend.item.EntityItem;
import com.example.printifybackend.jdbi.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.printifybackend.jdbi.DaoFilterQueryBuilder.OrderType.*;

@Repository
public class DaoOrder extends BaseDao<EntityOrder> {

    @Autowired
    public DaoOrder(Jdbi jdbi) {
        super(EntityOrder.class, jdbi);
    }

    public List<EntityOrder> loadByCriteria(LazyCriteria criteria) {
        final DaoFilterQueryBuilder filterQueryBuilder = new DaoOrderQueryBuilder("SELECT * FROM printify.order o");
        // add limit offset condition
        filterQueryBuilder.buildAndAddLimitOffsetStatement(criteria.getLimit(), criteria.getOffset());
        filterQueryBuilder.buildAndAddOrderByStatement(DESC, "created");
        filterQueryBuilder.buildAndAddWhereStatement(criteria.getFilter());

        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery(filterQueryBuilder.build())
                        .bindMap(filterQueryBuilder.getBind())
                        .mapTo(EntityOrder.class)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Loads orders by criteria joined with order items and items
     */
    public List<JoinedEntity> loadByCriteriaWithItems(LazyCriteria criteria) {
        final DaoFilterQueryBuilder filterQueryBuilder = new DaoOrderQueryBuilder("""
                SELECT * FROM printify.order o
                LEFT JOIN printify.order_item oi ON oi.order_id = o.id
                LEFT JOIN printify.item i ON oi.item_id = i.id"""
        );
        // add limit offset condition
        filterQueryBuilder.buildAndAddLimitOffsetStatement(criteria.getLimit(), criteria.getOffset());
        filterQueryBuilder.buildAndAddOrderByStatement(DESC, "created");
        filterQueryBuilder.buildAndAddWhereStatement(criteria.getFilter());

        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery(filterQueryBuilder.build())
                        .bindMap(filterQueryBuilder.getBind())
                        .reduceRows(
                                new JoinEntityRowReducer<>(
                                        EntityOrder.class,
                                        Pair.of(EntityOrderItem.class, JoinedEntity.JoinType.ONE_TO_MANY),
                                        Pair.of(EntityItem.class, JoinedEntity.JoinType.ONE_TO_MANY)
                                ))
                        .toList()
        );
    }

    /**
     * Loads orders by criteria joined with contact info, order items and items
     */
    public List<JoinedEntity> loadFullByCriteria(LazyCriteria criteria) {
        final DaoFilterQueryBuilder filterQueryBuilder = new DaoOrderQueryBuilder("""
                SELECT * FROM printify.order o
                JOIN printify.contact_info ci ON o.contact_info_id = ci.id
                LEFT JOIN printify.order_item oi ON oi.order_id = o.id
                LEFT JOIN printify.item i ON oi.item_id = i.id"""
        );
        // add limit offset condition
        filterQueryBuilder.buildAndAddLimitOffsetStatement(criteria.getLimit(), criteria.getOffset());
        filterQueryBuilder.buildAndAddOrderByStatement(DESC, "created");
        filterQueryBuilder.buildAndAddWhereStatement(criteria.getFilter());

        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery(filterQueryBuilder.build())
                        .bindMap(filterQueryBuilder.getBind())
                        .reduceRows(
                                new JoinEntityRowReducer<>(
                                        EntityOrder.class,
                                        Pair.of(EntityContactInfo.class, JoinedEntity.JoinType.ONE_TO_ONE),
                                        Pair.of(EntityOrderItem.class, JoinedEntity.JoinType.ONE_TO_MANY),
                                        Pair.of(EntityItem.class, JoinedEntity.JoinType.ONE_TO_MANY)
                                ))
                        .toList()
        );
    }

    private static class DaoOrderQueryBuilder extends DaoFilterQueryBuilder {

        protected DaoOrderQueryBuilder(String select) {
            super(select);
        }

        @SuppressWarnings("squid:S1192")
        @Override
        public void buildAndAddWhereStatement(Map<String, Object> filters) {
            if (filters == null || filters.isEmpty()) return;

            final WhereConditionBuilder whereBuilder = new WhereConditionBuilder();

            if (filters.containsKey("id") && filters.get("id") != null) {
                final Long id = ((Number) filters.get("id")).longValue();
                whereBuilder.addCondition("o.id = :id");
                this.getBind().put("id", id);
            }

            if (filters.containsKey("dateFrom") && filters.get("dateFrom") != null) {
                final LocalDateTime dateFrom = LocalDateTime.parse((String) filters.get("dateFrom"));
                whereBuilder.addCondition(":dateFrom <= created");
                this.getBind().put("dateFrom", dateFrom);
            }

            if (filters.containsKey("dateTo") && filters.get("dateTo") != null) {
                final LocalDateTime dateTo = LocalDateTime.parse((String) filters.get("dateTo"));
                whereBuilder.addCondition(":dateTo >= created");
                this.getBind().put("dateTo", dateTo);
            }

            this.setWhereStatement(whereBuilder.buildWhere());
        }
    }
}
