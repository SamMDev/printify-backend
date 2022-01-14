package com.example.coderamabackend.jdbi;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.core.mapper.Nested;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EntityReflect {

    private static final Map<Class<?>, EntityData<?>> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <E> EntityData<E> of(Class<E> entityClass) {
        EntityReflect.cache.computeIfAbsent(entityClass, ec -> new EntityData<>(entityClass, null));
        return (EntityData<E>) EntityReflect.cache.get(entityClass);
    }

    @Getter
    public static class EntityData<E> {
        private final Class<E> type;
        private final String tableName;

        private final String idColumnName;
        private final Set<String> columnNames = new LinkedHashSet<>();
        private final Map<String, FieldData> columnNameToFieldDataMap = new HashMap<>();
        private final Map<String, FieldData> filedNameToFieldDataMap = new HashMap<>();
        private final List<Pair<String, String>> columnNameFieldNamePairs = new ArrayList<>();
        private final List<Pair<String, String>> columnNameFieldNamePairsWithoutId = new ArrayList<>();
        private final Map<Class<?>, Method> annotatedMethodsMap = new HashMap<>();

        private EntityData(Class<E> entityClass, FieldData parentFieldData) {
            if (!entityClass.isAnnotationPresent(Table.class))
                throw new IllegalArgumentException("EntityReflect class requires the @Table annotation be present on the entity");

            this.type = entityClass;
            this.tableName = entityClass.getAnnotation(Table.class).name();
            List<Class<?>> hierarchy = new ArrayList<>();
            hierarchy.add(entityClass);
            while (hierarchy.get(0).getSuperclass() != Object.class) hierarchy.add(0, hierarchy.get(0).getSuperclass());
            final List<Field> allFields = hierarchy.stream().flatMap(c -> Arrays.stream(c.getDeclaredFields())).collect(Collectors.toList());
            String idColumnName = null;
            for (Field f : allFields) {

                final boolean nestedAnnotationPresent = f.isAnnotationPresent(Nested.class);
                if (!f.isAnnotationPresent(Id.class) && !f.isAnnotationPresent(Column.class) && !nestedAnnotationPresent) continue;

                final FieldData fieldData = new FieldData(f, parentFieldData, this.tableName);
                if (f.isAnnotationPresent(Id.class)) idColumnName = fieldData.columnName;

                if (nestedAnnotationPresent) {
                    final EntityData<?> nestedEntityData = new EntityData<>(f.getType(), fieldData);
                    this.columnNames.addAll(nestedEntityData.getColumnNames());
                    this.columnNameToFieldDataMap.putAll(nestedEntityData.columnNameToFieldDataMap);
                    this.filedNameToFieldDataMap.put(fieldData.name, fieldData);
                } else {
                    final String columnName = fieldData.getColumnName();
                    this.columnNames.add(columnName);
                    this.columnNameToFieldDataMap.put(columnName, fieldData);
                    this.filedNameToFieldDataMap.put(fieldData.name, fieldData);
                }
            }

            this.idColumnName = idColumnName;

            for (String columnName : this.columnNames) {
                final FieldData fd = this.columnNameToFieldDataMap.get(columnName);
                final String propertyName = fd.getParentFieldData() != null ? fd.getParentFieldData().getName() + "." + fd.getName() : fd.getName();
                final Pair<String, String> pair = Pair.of(columnName, propertyName);
                this.columnNameFieldNamePairs.add(pair);
                if (!fd.isId()) this.columnNameFieldNamePairsWithoutId.add(pair);
            }

            for (Method m : hierarchy.stream().flatMap(c -> Arrays.stream(c.getDeclaredMethods())).collect(Collectors.toList())) {
                for (Annotation a : m.getAnnotations()) {
                    this.annotatedMethodsMap.put(a.annotationType(), m);
                }
            }
        }
    }

    @Getter
    public static class FieldData {
        private final String name;

        private final String tableName;
        private final Class<?> type;
        private final String columnName;
        private final Id idAnnotation;
        private final Column columnAnnotation;
        private final ManyToOne manyToOneAnnotation;
        private final FieldData parentFieldData;
        private final Field field;

        public FieldData(Field field, FieldData parentFieldData, String tableName) {
            this.name = field.getName();
            this.tableName = tableName;
            this.type = field.getType();
            this.idAnnotation = field.getAnnotation(Id.class);
            this.columnAnnotation = field.getAnnotation(Column.class);
            this.manyToOneAnnotation = field.getAnnotation(ManyToOne.class);
            this.parentFieldData = parentFieldData;
            this.field = field;
            final String name = idAnnotation != null ? "id" : columnAnnotation != null ? columnAnnotation.name() : "";
            this.columnName = parentFieldData != null ? parentFieldData.field.getAnnotation(Nested.class).value() + name : name;
        }

        public boolean isId() {
            return this.idAnnotation != null;
        }

        public boolean isNested() {
            return parentFieldData != null;
        }

        public int getColumnLength() {
            return this.columnAnnotation.length();
        }

        public boolean isForeignKey() {
            return this.manyToOneAnnotation != null;
        }

        public Class<?> getFkTargetClass() {
            return this.manyToOneAnnotation != null ? this.manyToOneAnnotation.targetEntity() : null;
        }

        public boolean isUnique() {
            return this.columnAnnotation != null && this.columnAnnotation.unique();
        }

        public boolean isNullable() {
            return this.columnAnnotation != null && this.columnAnnotation.nullable();
        }
    }

}
