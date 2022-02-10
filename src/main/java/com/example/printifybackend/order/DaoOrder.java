package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.BaseDao;
import com.example.printifybackend.jdbi.LazyCriteria;
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

    public List<EntityOrder> getOrdersWithCriteria(LazyCriteria lazyCriteria) {
        final Map<String, Object> bind = new HashMap<>();

        String whereStatement = this.buildWhereStatement(lazyCriteria.getFilters(), bind);
        String order = " ORDER BY date desc ";
        String offsetLimit = this.buildLimitOffsetStatement(lazyCriteria.getLimit(), lazyCriteria.getOffset());

        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT * FROM printify.order " + whereStatement + order + offsetLimit)
                                .bindMap(bind)
                                .mapTo(EntityOrder.class)
                                .list()
        );
    }

    @Override
    public String buildWhereStatement(Map<String, Object> filters, Map<String, Object> bind) {

        final StringBuilder whereBuilder = new StringBuilder();

        if (!filters.isEmpty()) whereBuilder.append(" WHERE ");
        else return whereBuilder.toString();

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

        return whereBuilder.toString();
    }

    @Override
    public Long totalRowCount(Map<String, Object> filters) {

        final Map<String, Object> bind = new HashMap<>();
        final String whereStatement = this.buildWhereStatement(filters, bind);

        return this.jdbi.withHandle(
                handle -> handle
                        .createQuery("SELECT count(*) FROM printify.order " + whereStatement)
                        .bindMap(bind)
                        .mapTo(Long.class)
                        .one()
        );
    }
}
