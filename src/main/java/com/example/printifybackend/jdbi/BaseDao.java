package com.example.printifybackend.jdbi;

import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseDao<E extends BaseEntity> {
    private static final String INSERT_ENTITY_TEMPLATE = "INSERT INTO <NAME> (<COLUMNS>) VALUES (<VALUES>)";
    private static final String UPDATE_ENTITY_TEMPLATE = "UPDATE <NAME> SET <SET> WHERE id = :id";
    private static final String DELETE_BY_ID_TEMPLATE = "DELETE FROM <NAME> WHERE id = :id";
    private static final String FIND_ALL_TEMPLATE = "SELECT * FROM <NAME>";
    private static final String FIND_BY_ID_TEMPLATE = "SELECT * FROM <NAME> WHERE id = :id";

    private final EntityReflect.EntityData<? extends BaseEntity> reflect;

    private final String INSERT_ENTITY;
    private final String UPDATE_ENTITY;
    private final String DELETE_BY_ID;
    private final String FIND_ALL;
    private final String FIND_BY_ID;

    protected final Jdbi jdbi;

    public BaseDao(Class<E> clazz, Jdbi jdbi) {

        this.jdbi = jdbi;

        reflect = EntityReflect.of(clazz);

        final String TABLE_NAME = "printify." + reflect.getTableName();
        // names of columns in table (according to @Column annotation)
        final List<String> COLS_NAMES = new ArrayList<>();
        // names of entity data fields
        final List<String> FIELD_NAMES = new ArrayList<>();

        reflect.getColumnNameFieldNamePairsWithoutId()
                .forEach(pair -> {
                    COLS_NAMES.add(pair.getLeft());
                    FIELD_NAMES.add(":" + pair.getRight());
                });

        INSERT_ENTITY = INSERT_ENTITY_TEMPLATE
                .replace("<NAME>", TABLE_NAME)
                // inserting without id
                .replace("<COLUMNS>", StringUtils.join(COLS_NAMES, ","))
                .replace("<VALUES>", StringUtils.join(FIELD_NAMES, ","));

        UPDATE_ENTITY = UPDATE_ENTITY_TEMPLATE
                .replace("<NAME>", TABLE_NAME)
                // don't ever want to update ID
                .replace("<SET>", StringUtils.join(
                        reflect.getColumnNameFieldNamePairsWithoutId().stream().map(pair -> pair.getLeft() + " = :" + pair.getRight()).collect(Collectors.toList()),
                        ",")
                );

        DELETE_BY_ID = DELETE_BY_ID_TEMPLATE
                .replace("<NAME>", TABLE_NAME);

        FIND_ALL = FIND_ALL_TEMPLATE
                .replace("<NAME>", TABLE_NAME);

        FIND_BY_ID = FIND_BY_ID_TEMPLATE
                .replace("<NAME>", TABLE_NAME);
    }

    public void insert(E entity) {
        this.jdbi.withHandle(handle -> handle.createUpdate(INSERT_ENTITY).bindBean(entity).execute());
    }

    public void update(E entity) {
        this.jdbi.withHandle(handle -> handle.createUpdate(UPDATE_ENTITY).bindBean(entity).execute());
    }

    public E findById(Long id) {
        return (E) this.jdbi.withHandle(
                handle ->
                        handle
                                .createQuery(FIND_BY_ID)
                                .bind("id", id)
                                .mapToBean(reflect.getType())
                                .stream().findFirst().orElse(null));
    }

    public void deleteById(Long id) {
        this.jdbi.withHandle(handle -> handle.createUpdate(DELETE_BY_ID).bind("id", id).execute());
    }

    public List<E> findAll() {
        return (List<E>) this.jdbi.withHandle(handle -> handle
                                .createQuery(FIND_ALL)
                                .mapToBean(reflect.getType())
                                .list()
        );
    }
}
