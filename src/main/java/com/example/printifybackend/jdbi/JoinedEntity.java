package com.example.printifybackend.jdbi;

import java.util.*;

/**
 * Joined entity represents primary entity joined with another entities
 * These are joined either with oneToOne relationship or oneToMany relationship
 * They are stored in map relationship <Entity type (class), List of joined entities (oneToMany) or entity itself (oneToOne)>
 *
 * @author Samuel Molčan
 */
public class JoinedEntity extends LinkedHashMap<Class<?>, Object> {

    private final BaseEntity primaryEntity;

    public <E extends BaseEntity> JoinedEntity(E primaryEntity) {
        this.primaryEntity = primaryEntity;
        this.put(primaryEntity.getClass(), this.primaryEntity);
    }

    public <J extends BaseEntity> void addJoinedEntity(J entity, JoinType join) {
        if (entity == null) return;

        if (join == JoinType.ONE_TO_ONE) this.put(entity.getClass(), entity);
        else {
            final Set<J> joinedEntitiesSet = (Set<J>) this.computeIfAbsent(entity.getClass(), id -> new LinkedHashSet<J>());
            joinedEntitiesSet.add(entity);
        }
    }

    public <J extends BaseEntity> Set<J> getJoinedMany(Class<J> clazz) {
        return (Set<J>) Optional.ofNullable(this.getOrDefault(clazz, null)).orElse(Collections.emptySet());
    }

    public <J extends BaseEntity> J getJoinedOne(Class<J> clazz) {
        return (J) this.getOrDefault(clazz, null);
    }

    public enum JoinType {
        ONE_TO_ONE,
        ONE_TO_MANY
    }


}
