package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.BaseDao;
import com.example.printifybackend.jdbi.DaoFilterQueryBuilder;
import com.example.printifybackend.jdbi.LazyCriteria;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        final DaoFilterQueryBuilder filterQueryBuilder = new DaoOrderQueryBuilder("SELECT * FROM printify.order");
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

    private static class DaoOrderQueryBuilder extends DaoFilterQueryBuilder {

        protected DaoOrderQueryBuilder(String select) {
            super(select);
        }

        @Override
        public void buildAndAddWhereStatement(Map<String, Object> filters) {
            if (filters == null || filters.isEmpty()) return;

            final WhereConditionBuilder whereBuilder = new WhereConditionBuilder();

            if (filters.containsKey("id") && filters.get("id") != null) {
                final Long id = ((Number) filters.get("id")).longValue();
                whereBuilder.addCondition("id = :id");
                this.getBind().put("id", id);
            }

            this.setWhereStatement(whereBuilder.buildWhere());
        }
    }
}
