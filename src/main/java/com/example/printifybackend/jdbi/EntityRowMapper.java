package com.example.printifybackend.jdbi;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.persistence.PostLoad;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Row mapper used for mapping any entity
 *
 * @param <E>
 */
public class EntityRowMapper<E> implements RowMapper<E> {
    private final EntityReflect.EntityData<E> entityData;

    public EntityRowMapper(Class<E> clazz) {
        this.entityData = EntityReflect.of(clazz);
    }

    @Override
    public E map(ResultSet rs, StatementContext ctx) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();

        E obj;
        try {
            obj = this.entityData.getType().getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("A type, %s, was mapped which was not instantiable", this.entityData.getType().getName()), e);
        }

        // looping through columns in result set
        for (int i = 1; i <= metadata.getColumnCount(); ++i) {
            // name of table and entity table annotation must equal
            if (!this.entityData.getTableName().equalsIgnoreCase(metadata.getTableName(i))) continue;

            // name of currently looped column
            String name = metadata.getColumnLabel(i).toLowerCase();
            final EntityReflect.FieldData fieldData = this.entityData.getColumnNameToFieldDataMap().getOrDefault(name, null);
            if (fieldData == null) continue;


            if (fieldData.isNested()) {
                final EntityReflect.FieldData parentFieldData = fieldData.getParentFieldData();
                Object nestedObject = getOrCreateNestedObject(obj, parentFieldData);
                EntityRowMapper.setObjectField(nestedObject, fieldData.getField(), i, rs, ctx);
                continue;
            }
            EntityRowMapper.setObjectField(obj, fieldData.getField(), i, rs, ctx);
        }

        if (this.entityData.getAnnotatedMethodsMap().containsKey(PostLoad.class)) {
            final Method method = this.entityData.getAnnotatedMethodsMap().get(PostLoad.class);
            try {
                method.setAccessible(true);
                method.invoke(obj);
            }catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(String.format("Unable to invoke PostConstruct method, %s", method.getName()), e);
            }
        }
        return obj;
    }

    private Object getOrCreateNestedObject(E obj, EntityReflect.FieldData fieldData) {
        Object nestedObject;
        try {
            fieldData.getField().setAccessible(true);
            nestedObject = fieldData.getField().get(obj);
            if (nestedObject == null) {
                nestedObject = fieldData.getType().getDeclaredConstructor().newInstance();
                fieldData.getField().set(obj, nestedObject);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Unable to access nested property, %s", fieldData.getName()), e);
        }
        return nestedObject;
    }

    private static void setObjectField(Object obj, Field field, int columnIndex, ResultSet rs, StatementContext ctx) throws SQLException {
        final Type type = field.getGenericType();
        final ColumnMapper<?> mapper = ctx.findColumnMapperFor(type).orElse((resultSet, number, context) -> resultSet.getObject(number));

        Object value = mapper.map(rs, columnIndex, ctx);
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Unable to access property, %s", field.getName()), e);
        }
    }

}
