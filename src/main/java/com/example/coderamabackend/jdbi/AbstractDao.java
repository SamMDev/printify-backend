package com.example.coderamabackend.jdbi;


import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.jdbi.v3.sqlobject.SqlObject;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public interface AbstractDao<E extends BaseEntity> extends SqlObject {

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

}
