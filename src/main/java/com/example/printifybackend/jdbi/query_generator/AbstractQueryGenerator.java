package com.example.printifybackend.jdbi.query_generator;

import com.example.printifybackend.jdbi.BaseEntity;
import com.example.printifybackend.jdbi.reflect.EntityReflect;
import lombok.NonNull;

import java.util.Optional;

public abstract class AbstractQueryGenerator {
    protected final EntityReflect<? extends BaseEntity> reflect;
    protected final String tableName;
    private String resultQuery;

    protected abstract String generate();

    protected  <E extends BaseEntity> AbstractQueryGenerator(@NonNull EntityReflect<E> reflect) {
        this.reflect = reflect;
        this.tableName = Optional.ofNullable(reflect.getTableName()).map(name -> "printify." + name)
                .orElseThrow(() -> new QueryGeneratorException(String.format("Entity reflection of type %s does not have table name", reflect.getClazz().getName())));
    }

    public String getResult() {
        if (this.resultQuery == null) this.resultQuery = this.generate();
        return this.resultQuery;
    }


    public static class QueryGeneratorException extends RuntimeException {

        public QueryGeneratorException(String message) {
            super("Could not generate query: " + message);
        }

    }
}
