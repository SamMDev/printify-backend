package com.example.printifybackend.jdbi;

import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * BaseDao all the DAO's should extend
 * Provides common functionality for all the DAO's
 *
 * @param <E> entity clazz
 * @author Samuel Molčan
 */
@SuppressWarnings("squid:S1192")
public abstract class BaseDao<E extends BaseEntity> {
    private static final String INSERT_ENTITY_TEMPLATE = "INSERT INTO <NAME> (<COLUMNS>) VALUES (<VALUES>)";
    private static final String UPDATE_ENTITY_TEMPLATE = "UPDATE <NAME> SET <SET> WHERE id = :id";
    private static final String DELETE_BY_ID_TEMPLATE = "DELETE FROM <NAME> WHERE id = :id";
    private static final String FIND_ALL_TEMPLATE = "SELECT * FROM <NAME>";
    private static final String FIND_BY_ID_TEMPLATE = "SELECT * FROM <NAME> WHERE id = :id";

    private final EntityReflectionManager.EntityReflect<E> reflect;

    private final String insertEntityQuery;
    private final String updateEntityQuery;
    private final String deleteByIdQuery;
    private final String findAllQuery;
    private final String findByIdQuery;

    protected final Jdbi jdbi;

    protected BaseDao(Class<E> clazz, Jdbi jdbi) {

        this.jdbi = jdbi;

        this.reflect = EntityReflectionManager.reflectionOf(clazz);

        final String TABLE_NAME = "printify." + reflect.getTableName();

        this.insertEntityQuery = INSERT_ENTITY_TEMPLATE
                .replace("<NAME>", TABLE_NAME)
                // inserting without id
                .replace("<COLUMNS>", String.join(",", reflect.getColumnNamesWithoutId()))
                .replace("<VALUES>", String.join(",", reflect.getColumnNamesWithoutId().stream().map(c -> ":" + c).toList()));

        this.updateEntityQuery = UPDATE_ENTITY_TEMPLATE
                .replace("<NAME>", TABLE_NAME)
                // don't ever want to update ID
                .replace("<SET>", String.join(
                        ",",
                        reflect.getColumnNamesWithoutId().stream().map(pair -> String.format("%s = :%s", pair, pair)).toList())
                );

        this.deleteByIdQuery = DELETE_BY_ID_TEMPLATE
                .replace("<NAME>", TABLE_NAME);

        this.findAllQuery = FIND_ALL_TEMPLATE
                .replace("<NAME>", TABLE_NAME);

        this.findByIdQuery = FIND_BY_ID_TEMPLATE
                .replace("<NAME>", TABLE_NAME);
    }

    public void insert(E entity) {
        this.jdbi.withHandle(handle -> handle.createUpdate(insertEntityQuery).bindBean(entity).execute());
    }

    public void update(E entity) {
        this.jdbi.withHandle(handle -> handle.createUpdate(updateEntityQuery).bindBean(entity).execute());
    }

    public E findById(Long id) {
        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery(findByIdQuery)
                                .bind("id", id)
                                .mapTo(reflect.getClazz())
                                .stream().findFirst().orElse(null));
    }

    public void deleteById(Long id) {
        this.jdbi.withHandle(handle -> handle.createUpdate(deleteByIdQuery).bind("id", id).execute());
    }

    public List<E> findAll() {
        return this.jdbi.withHandle(handle -> handle
                .createQuery(findAllQuery)
                .mapTo(reflect.getClazz())
                .list()
        );
    }

}
