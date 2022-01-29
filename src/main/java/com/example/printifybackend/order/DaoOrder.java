package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DaoOrder extends BaseDao<EntityOrder> {

    @Autowired
    public DaoOrder(Jdbi jdbi) {
        super(EntityOrder.class, jdbi);
    }
}
