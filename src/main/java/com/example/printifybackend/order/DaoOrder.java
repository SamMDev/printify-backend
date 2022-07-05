package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.BaseDao;
import com.example.printifybackend.jdbi.DaoFilterQueryBuilder;
import com.example.printifybackend.jdbi.LazyCriteria;
import com.example.printifybackend.utils.DateUtils;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class DaoOrder extends BaseDao<EntityOrder> {

    @Autowired
    public DaoOrder(Jdbi jdbi) {
        super(EntityOrder.class, jdbi);
    }

    public List<EntityOrder> getOrdersWithCriteria(LazyCriteria lazyCriteria) {
        final DaoFilterQueryBuilder orderQueryBuilder = new OrderQueryBuilder("SELECT * FROM printify.order ");
        orderQueryBuilder.buildWhereStatement(lazyCriteria.getFilter());
        orderQueryBuilder.buildLimitOffsetStatement(lazyCriteria.getLimit(), lazyCriteria.getOffset());
        orderQueryBuilder.setOrderStatement("ORDER BY date desc");

        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery(orderQueryBuilder.build())
                                .bindMap(orderQueryBuilder.getBind())
                                .mapTo(EntityOrder.class)
                                .list()
        );
    }

    public Long totalRowCount(Map<String, Object> filters) {
        final DaoFilterQueryBuilder orderQueryBuilder = new OrderQueryBuilder("SELECT count(*) FROM printify.order ");
        orderQueryBuilder.buildWhereStatement(filters);

        return this.jdbi.withHandle(
                handle -> handle
                        .createQuery(orderQueryBuilder.build())
                        .bindMap(orderQueryBuilder.getBind())
                        .mapTo(Long.class)
                        .one()
        );
    }

    public static class OrderQueryBuilder extends DaoFilterQueryBuilder {

        public OrderQueryBuilder(String select) {
            super(select);
        }

        @Override
        public void buildWhereStatement(Map<String, Object> filters) {
            if (filters == null || filters.isEmpty()) return;

            final WhereConditionBuilder whereConditionBuilder = new WhereConditionBuilder();

            final String finishedKey = "finished";
            if (filters.containsKey(finishedKey)) {
                Boolean value = (Boolean) filters.get(finishedKey);
                super.getBind().put(finishedKey, value);
                whereConditionBuilder.addCondition(" (finished = :finished) ");
            }

            final String fromDateKey = "fromDate";
            if (filters.containsKey(fromDateKey)) {
                final LocalDateTime startOfDay = DateUtils.jsDateToLocalDateTime((String) filters.get(fromDateKey));
                if (startOfDay != null) {
                    super.getBind().put(fromDateKey, startOfDay);
                    whereConditionBuilder.addCondition(" (date >= :fromDate) ");
                }
            }

            final String toDateKey = "toDate";
            if (filters.containsKey(toDateKey)) {
                final LocalDateTime endOfDay = DateUtils.jsDateToLocalDateTime((String) filters.get(toDateKey));
                if (endOfDay != null) {
                    super.getBind().put(toDateKey, endOfDay);
                    whereConditionBuilder.addCondition( " (date <= :toDate) " );
                }
            }

            super.setWhereStatement(whereConditionBuilder.buildWhere());
        }

    }
}
