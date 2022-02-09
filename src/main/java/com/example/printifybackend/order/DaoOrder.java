package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.BaseDao;
import com.example.printifybackend.jdbi.Criteria;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DaoOrder extends BaseDao<EntityOrder> {

    @Autowired
    public DaoOrder(Jdbi jdbi) {
        super(EntityOrder.class, jdbi);
    }

    public List<EntityOrder> getOrdersWithCriteria(Criteria criteria) {
        final Map<String, Object> bind = new HashMap<>();
        final Map<String, Object> filters = criteria.getFilters();

        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append(" WHERE ");

        if (filters.containsKey("finished")) {
            Boolean value = (Boolean) filters.get("finished");
            bind.put("finished", value);
            whereBuilder.append(" (finished = :finished) ");
        }

        if (filters.containsKey("fromDate")) {
            final LocalDateTime startOfDay = ((LocalDate) filters.get("fromDate")).atStartOfDay();
            bind.put("fromDate", startOfDay);
            whereBuilder.append(" AND date >= :fromDate ");
        }

        if (filters.containsKey("toDate")) {
            final LocalDateTime endOfDay = ((LocalDate) filters.get("fromDate")).atTime(LocalTime.MAX);
            bind.put("toDate", endOfDay);
            whereBuilder.append( " AND date <= :toDate " );
        }

        whereBuilder.append(" ORDER BY date desc ");
        whereBuilder.append(this.getLimitOffsetQuery(criteria.getLimit(), criteria.getOffset()));

        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT * FROM printify.order " + whereBuilder)
                                .bindMap(bind)
                                .mapTo(EntityOrder.class)
                                .list()
        );

    }
}
