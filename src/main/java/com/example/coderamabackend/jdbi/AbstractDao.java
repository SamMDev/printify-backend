package com.example.coderamabackend.jdbi;


import com.example.coderamabackend.item.EntityItem;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.jdbi.v3.sqlobject.SqlObject;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public interface AbstractDao<E extends BaseEntity> extends SqlObject {

    String insertEntity = "INSERT INTO <NAME> (<COLUMNS>) VALUES (<VALUES>)";
    String updateEntity = "UPDATE <NAME> (<COLUMNS>) VALUES (<VALUES>)";

    default void insert(E entity) {
        final Update query = getHandle()
                .createUpdate(getInsertQuery(entity.getClass()))
                .bindBean(entity);
        query.execute();
    }

    default void deleteById(long id) {
        final Update delete = getHandle().createUpdate("DELETE FROM " + "printify." + this.getTableName() + " WHERE id = :id");
        delete.bind("id", id);
        delete.execute();
    }

    default List<E> findAll() {
        return this.getHandle()
                .createQuery("SELECT * FROM " + "printify." + this.getTableName())
                .mapTo(getEntityClass()).list();
    }

    default E findById(Long id){
        final Query query = getHandle().createQuery("SELECT * FROM " + "printify." + this.getTableName() + " WHERE id = :id");
        query.bind("id", id);
        return query.mapTo(getEntityClass()).one();
    }




    default String getTableName() {
        Class<E> clazz = this.getEntityClass();
        if (!clazz.isAnnotationPresent(Table.class)) throw new IllegalStateException("Entity class must have the @Table annotation defined");
        return clazz.getAnnotation(Table.class).name();
    }

    default Class<E> getEntityClass(){
        final Type genericSuperclass = this.getClass().getGenericInterfaces()[0];
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            // proxy
            return (Class<E>) ((ParameterizedType) this.getClass().getInterfaces()[0].getGenericInterfaces()[0]).getActualTypeArguments()[0];
        }
    }

    default String getInsertQuery(Class<? extends BaseEntity> clazz) {
        EntityReflect.EntityData<? extends BaseEntity> reflect = EntityReflect.of(clazz);

        String tableName = "printify." + reflect.getTableName();
        List<String> colsNames = new ArrayList<>();
        List<String> fieldsNames = new ArrayList<>();
        reflect.getColumnNameFieldNamePairsWithoutId()
                .forEach(pair -> {
                    colsNames.add(pair.getLeft());
                    fieldsNames.add(":" + pair.getRight());
                });

        return insertEntity
                .replace("<NAME>", tableName)
                .replace("<COLUMNS>", StringUtils.join(colsNames, ","))
                .replace("<VALUES>", StringUtils.join(fieldsNames, ","));
    }

}
