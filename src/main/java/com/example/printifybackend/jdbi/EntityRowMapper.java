package com.example.printifybackend.jdbi;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class EntityRowMapper<E extends BaseEntity> implements RowMapper<E> {

    private final EntityReflectionManager.EntityReflect<E> reflect;

    public EntityRowMapper(Class<E> entityClazz) {
        this.reflect = EntityReflectionManager.reflectionOf(entityClazz);
    }

    @Override
    public E map(ResultSet rs, StatementContext ctx) throws SQLException {
        final ResultSetMetaData metaData = rs.getMetaData();

        E entity = this.reflect.getInstance();

        // every column from resultSet must be mapped to field in entity
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {

            // table name of current column must be same as @Table name annotation in entity
            if (!metaData.getTableName(columnIndex).equals(this.reflect.getTableName())) continue;

            // find field in entity that has @Column(name=?) annotation where name matches ResultSet column name
            final String columnName = metaData.getColumnName(columnIndex);
            final Field entityField = this.reflect.getColumnAnnotatedFieldByName(columnName);
            if (entityField == null) continue;

            // type of field + find mapper for it (column types are usually basic, jdbi provides mappers for them)
            final Type type = entityField.getGenericType();
            // find mapper for given type, or just leave object as it is
            final ColumnMapper<?> mapper = ctx.findColumnMapperFor(type).orElse((resultSet, columnNum, statementContext) -> resultSet.getObject(columnNum));

            // find the value and set it to field
            try {
                // get value from result set
                Object value = mapper.map(rs, columnIndex, ctx);
                // set it to object field
                this.reflect.setFieldValue(entity, entityField, value);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        String.format("Could not set field value for entity %s and field %s", entity.getClass().getName(), entityField.getName()), e);
            }
        }

        return entity;
    }
}
