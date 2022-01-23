package com.example.coderamabackend;

import com.example.coderamabackend.jdbi.JoinedEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Converter used for mapping between objects
 * @author SamMDev
 */
public class DtoConverter {

    @Getter
    private static final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Converts given object into given clazz object
     * @param object    to convert
     * @param clazz     to convert to
     * @param <O>       type of object
     * @param <C>       type of class to convert to
     * @return          converted to new clazz
     */
    public static <O, C> C convert(O object, Class<C> clazz) {
        if (object == null || clazz == null) return null;
        return objectMapper.convertValue(object, clazz);
    }

    /**
     * Reads given string into clazz object
     *
     * @param serialized    object as json string
     * @param clazz         to be converted to
     * @param <C>           type of class
     * @return              deserialized object
     */
    @SneakyThrows
    public static <C> C read(String serialized, Class<C> clazz) {
        if (serialized == null || clazz == null) return null;
        return objectMapper.readValue(serialized, clazz);
    }

    /**
     * Maps joined entity into one DTO object
     * Unrecognized fields are skipped
     *
     * @param joined    joined entity
     * @param dtoClazz  joined entity to be mapped to
     * @param <C>       type of dto class
     * @return          dto class object mapped
     */
    public static <C> C convertJoined(JoinedEntity joined, Class<C> dtoClazz) {
        if (joined == null || dtoClazz == null) return null;

        Object dto;
        try {
            dto = dtoClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

        final List<Method> dtoClazzSetters = Arrays
                .stream(dtoClazz.getMethods())
                .filter(m -> m.getName().startsWith("set"))
                .collect(Collectors.toList());

        // for each entity in joined entity
        for (String clazzName : joined.keySet()) {

            Object entity = joined.get(clazzName);
            final List<Method> entityGetters = Arrays
                    .stream(entity.getClass().getDeclaredMethods())
                    .filter(m -> m.getName().startsWith("get"))
                    .collect(Collectors.toList());

            for (Method entityGetter : entityGetters) {
                // find dto setter for current entity getter
                Method dtoSetter = dtoClazzSetters
                        .stream()
                        .filter(setter -> Objects.equals(setter.getName(), entityGetter.getName().replace("get", "set")))
                        .findFirst().orElse(null);
                if (dtoSetter == null) continue;

                try {
                    // map value from entity getter to dto setter
                    dtoSetter.invoke(dto, entityGetter.invoke(entity));
                } catch (Exception ignored) {}
            }
        }
        return (C) dto;
    }

}
