package com.example.printifybackend.jdbi.query_generator;

import com.example.printifybackend.jdbi.BaseEntity;
import com.example.printifybackend.jdbi.reflect.EntityReflect;
import lombok.NonNull;

public class FindByIdQueryGenerator extends AbstractQueryGenerator {

    private static final String FIND_BY_ID_TEMPLATE = "SELECT * FROM <NAME> WHERE id = :id";

    public <E extends BaseEntity> FindByIdQueryGenerator(@NonNull EntityReflect<E> reflect) {
        super(reflect);
    }

    @Override
    protected String generate() {
        return FIND_BY_ID_TEMPLATE.replace("<NAME>", this.tableName);
    }
}
