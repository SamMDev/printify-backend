package com.example.printifybackend.jdbi.query_generator;

import com.example.printifybackend.jdbi.BaseEntity;
import com.example.printifybackend.jdbi.reflect.EntityReflect;

public class UpdateQueryGenerator extends AbstractQueryGenerator {

    private static final String UPDATE_ENTITY_TEMPLATE = "UPDATE <NAME> SET <SET> WHERE id = :id";

    public <E extends BaseEntity> UpdateQueryGenerator(EntityReflect<E> reflect) {
        super(reflect);
    }

    @Override
    protected String generate() {
        final String setQueryPart = String.join(",", reflect.getColumnFieldsData()
                .stream()
                .map(fieldData -> String.format("%s = :%s", fieldData.getColumnName(), fieldData.getFieldName()))
                .toList());
        return UPDATE_ENTITY_TEMPLATE
                .replace("<NAME>", this.tableName)
                .replace("<SET>", setQueryPart);
    }
}
