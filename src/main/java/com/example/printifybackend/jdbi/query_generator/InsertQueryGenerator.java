package com.example.printifybackend.jdbi.query_generator;

import com.example.printifybackend.jdbi.BaseEntity;
import com.example.printifybackend.jdbi.reflect.EntityReflect;

/**
 * Query generator for insert statement
 */
public class InsertQueryGenerator extends AbstractQueryGenerator {

    private static final String INSERT_ENTITY_TEMPLATE = "INSERT INTO <NAME> (<COLUMNS>) VALUES (<VALUES>)";

    public <E extends BaseEntity> InsertQueryGenerator(EntityReflect<E> reflect) {
        super(reflect);
    }

    @Override
    protected String generate() {
        return INSERT_ENTITY_TEMPLATE
                .replace("<NAME>", this.tableName)
                // inserting without id
                .replace("<COLUMNS>", String.join(",", reflect.getColumnNamesWithoutId()))
                .replace("<VALUES>", String.join(",", reflect.getFieldNamesWithoutId().stream().map(c -> ":" + c).toList()));
    }
}
