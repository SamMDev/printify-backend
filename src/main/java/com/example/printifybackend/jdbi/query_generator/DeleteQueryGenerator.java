package com.example.printifybackend.jdbi.query_generator;

import com.example.printifybackend.jdbi.BaseEntity;
import com.example.printifybackend.jdbi.reflect.EntityReflect;
import lombok.NonNull;

public class DeleteQueryGenerator extends AbstractQueryGenerator {

    private static final String DELETE_BY_ID_TEMPLATE = "DELETE FROM <NAME> WHERE id = :id";

    public <E extends BaseEntity> DeleteQueryGenerator(@NonNull EntityReflect<E> reflect) {
        super(reflect);
    }

    @Override
    protected String generate() {
        return DELETE_BY_ID_TEMPLATE.replace("<NAME>", this.tableName);
    }
}
