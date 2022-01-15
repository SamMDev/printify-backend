package com.example.coderamabackend.order;

import com.example.coderamabackend.jdbi.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoOrder extends AbstractDao<EntityOrder> {
}
