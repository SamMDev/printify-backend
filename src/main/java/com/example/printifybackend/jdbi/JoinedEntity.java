package com.example.printifybackend.jdbi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JoinedEntity extends LinkedHashMap<String, Object> {

    @Getter
    private final Long id;

    private final Map<Class<?>, String> joinAliasMap;

    public <T extends BaseEntity> JoinedEntity(T primaryEntity, String primaryAlias, List<Join> joinClazzList) {
        this.id = primaryEntity.getId();
        this.joinAliasMap = joinClazzList.stream().collect(Collectors.toMap(Join::getEntityClass, Join::getAlias));
        this.joinAliasMap.put(primaryEntity.getClass(), primaryAlias);
        this.put(primaryAlias, primaryEntity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends BaseEntity> void addEntity(T entity, String alias, boolean oneToMany) {
        if (!oneToMany) {
            this.put(alias, entity);
            return;
        }
        this.computeIfAbsent(alias, p -> new LinkedHashSet<>());
        final Object set = this.get(alias);
        if (set instanceof Set) { //it's already a set lets just add another joined record
            ((Set)set).add(entity);
        }
    }

    public <T extends BaseEntity> T get(Class<T> clazz) {
        return this.get(clazz, this.joinAliasMap.getOrDefault(clazz, null));
    }

    @SuppressWarnings({"unchecked"})
    public <T extends BaseEntity> T get(Class<T> clazz, String alias) {
        if (alias == null) return null;
        return (T)this.getOrDefault(this.joinAliasMap.get(clazz), null);
    }

    public <T extends BaseEntity> Set<T> getMany(Class<T> clazz) {
        return this.getMany(clazz, this.joinAliasMap.getOrDefault(clazz, null));
    }

    @SuppressWarnings({"unchecked"})
    public <T extends BaseEntity> Set<T> getMany(Class<T> clazz, String alias) {
        if (alias == null) return null;
        return (Set<T>)this.getOrDefault(alias, Collections.emptySet());
    }

}
