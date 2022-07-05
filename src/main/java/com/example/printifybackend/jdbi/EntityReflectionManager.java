package com.example.printifybackend.jdbi;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Reflection manager providing all the services needed for reflection
 *
 * @author Samuel Molčan
 */
public class EntityReflectionManager {

    private EntityReflectionManager() {
    }

    // cache to store reflected entities
    private static final Map<Class<? extends BaseEntity>, EntityReflect<? extends BaseEntity>> cache = new HashMap<>();

    /**
     * Creates reflection of given entity clazz
     *
     * @param clazz given clazz
     * @param <E>   clazz type
     * @return reflected clazz
     */
    @SuppressWarnings("unchecked")
    public static <E extends BaseEntity> EntityReflect<E> reflectionOf(Class<E> clazz) {
        if (clazz == null) return null;
        EntityReflectionManager.cache.computeIfAbsent(clazz, ez -> new EntityReflect<>(clazz));
        return (EntityReflect<E>) EntityReflectionManager.cache.get(clazz);
    }

    /**
     * Get list of all the fields of given clazz
     *
     * @param clazz given clazz
     * @return list of its fields
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        if (cache.containsKey(clazz)) return cache.get(clazz).getFields();
        if (clazz == null) return Collections.emptyList();

        // add fields of given clazz
        final List<Field> allFields = new ArrayList<>(List.of(clazz.getDeclaredFields()));

        // add fields of all the parent clazz
        Class<?> superClazz = clazz.getSuperclass();
        while (superClazz != null) {
            allFields.addAll(List.of(superClazz.getDeclaredFields()));
            superClazz = superClazz.getSuperclass();
        }

        return allFields;
    }

    @Getter
    public static class EntityReflect<E> {

        private final String tableName;
        private final Class<E> clazz;
        private final List<Field> fields = new ArrayList<>();
        private final List<Field> columnAnnotatedField = new ArrayList<>();

        private final List<String> columnNamesWithoutId = new ArrayList<>();

        public EntityReflect(Class<E> clazz) {

            this.clazz = clazz;
            this.tableName =
                    Optional.ofNullable(
                            Optional.ofNullable(this.clazz.getAnnotation(Table.class))
                                    .orElseThrow(() -> new EntityReflectionException(String.format("Could not create entity reflection of %s, class is not annotated with @Table annotation", clazz.getName())))
                    ).map(Table::name).orElseThrow(() -> new EntityReflectionException(String.format("Could not create entity reflection of %s, class @Table annotation does not have table name", clazz.getName())));

            // loop through fields and save all the needed info
            for (Field field : EntityReflectionManager.getAllFields(clazz)) {
                // save every field
                this.fields.add(field);

                // save column annotated fields (also field annotated with @Id is mapped field)
                if (field.getAnnotation(Column.class) != null || field.getAnnotation(Id.class) != null) {
                    this.columnAnnotatedField.add(field);
                    // instead of ID field
                    if (field.getAnnotation(Id.class) == null) this.columnNamesWithoutId.add(field.getName());
                }
            }
        }

        /**
         * Finds field annotated by Column annotation with given name
         *
         * @param name given name
         * @return field
         */
        public Field getColumnAnnotatedFieldByName(String name) {
            if (name == null) return null;
            return this.columnAnnotatedField.stream()
                    .filter(field -> field.getName().equalsIgnoreCase(name))
                    .findFirst().orElse(null);
        }

        /**
         * Creates instance if reflected clazz
         *
         * @return instance object
         */
        public E getInstance() {
            if (this.getClazz() == null) return null;
            try {
                return this.getClazz().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new EntityReflectionException(
                        e, String.format("Could not create instance of %s entity, constructor could not be accessed", this.getClazz().getName())
                );
            }
        }

        /**
         * Having an object and its field, sets value
         *
         * @param obj   object we want to set field
         * @param field field to be set
         * @param value new field value
         */
        public void setFieldValue(E obj, Field field, Object value) {
            if (obj == null)
                throw new EntityReflectionException("Could not set field value to entity. Provided entity is null");
            if (field == null)
                throw new EntityReflectionException("Could not set field value to entity. Provided field is null");

            boolean accessibility = field.isAccessible();
            try {
                field.setAccessible(true);
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                throw new EntityReflectionException(e, "Could not set field value");
            } finally {
                field.setAccessible(accessibility);
            }
        }
    }


    public static class EntityReflectionException extends RuntimeException {
        public EntityReflectionException(Exception e, String message) {
            super(message, e);
        }

        public EntityReflectionException(String message) {
            super(message);
        }
    }
}
