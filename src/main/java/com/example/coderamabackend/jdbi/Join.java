package com.example.coderamabackend.jdbi;

import lombok.Getter;

@Getter
public class Join {
    private final Class<? extends BaseEntity> entityClass;

    private final String alias;
    private final boolean oneToMany;

    protected Join(Class<? extends BaseEntity> entityClass, String alias, boolean oneToMany) {
        this.entityClass = entityClass;
        this.alias = alias;
        this.oneToMany = oneToMany;
    }

    public static Join oneToOneEntity(Class<? extends BaseEntity> clazz, String alias) {
        return new Join(clazz, alias, false);
    }

    public static Join oneToManyEntity(Class<? extends BaseEntity> clazz, String alias) {
        return new Join(clazz, alias, true);
    }
}
