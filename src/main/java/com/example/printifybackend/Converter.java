package com.example.printifybackend;

import com.example.printifybackend.jdbi.JoinedEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Converter used for mapping between objects
 * @author SamMDev
 */
@SuppressWarnings("squid:S1118")    // no need to have private constructor
@Slf4j
public class Converter {

    @Getter
    private static final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
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
     * Maps joined entity with oneToOne relation into one DTO object
     * Unrecognized fields are skipped
     *
     * @param joined    joined entity
     * @param dtoClazz  joined entity to be mapped to
     * @param <C>       type of dto class
     * @return          dto class object mapped
     */
    public static <C> C accumulateJoinedToDto(JoinedEntity joined, Class<C> dtoClazz) {
        if (joined == null || dtoClazz == null) return null;
        if (joined.entrySet().stream().anyMatch(entry -> entry.getValue() instanceof Collection<?>))
            throw new IllegalArgumentException("When accumulating joined entity into one dto, all the relations in joined entity must be one-to-one");

        C dto;
        try {
            dto = dtoClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

        final List<Method> dtoClazzSetters = Arrays
                .stream(dtoClazz.getMethods())
                .filter(m -> m.getName().startsWith("set"))
                .toList();

        // for each entity in joined entity
        for (Map.Entry<Class<?>, Object> entry : joined.entrySet()) {

            Object entity = entry.getValue();
            final List<Method> entityGetters = Arrays
                    .stream(entity.getClass().getDeclaredMethods())
                    .filter(m -> m.getName().startsWith("get"))
                    .toList();

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
                } catch (Exception e) {
                    log.warn(String.format("Could not map set value with setter %s in class %s", dtoSetter.getName(), dtoClazz.getName()), e);
                }
            }
        }
        return dto;
    }

}
