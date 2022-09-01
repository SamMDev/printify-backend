package com.example.printifybackend.jdbi.reflect;

import com.example.printifybackend.jdbi.BaseEntity;

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


    public static class EntityReflectionException extends RuntimeException {
        public EntityReflectionException(Exception e, String message) {
            super(message, e);
        }

        public EntityReflectionException(String message) {
            super(message);
        }
    }
}
