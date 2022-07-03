package com.example.printifybackend.jdbi;

import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * Reduce joined tables into one single JoinedEntity object
 *
 * @param <E> primary entity type
 * @author Samuel Molčan
 */
public class JoinEntityRowReducer<E extends BaseEntity> implements RowReducer<LinkedHashMap<Long, JoinedEntity>, JoinedEntity> {

    private final Class<E> primaryEntityType;
    private final List<Pair<Class<? extends BaseEntity>, JoinedEntity.JoinType>> joinedClazzListPairs;

    public JoinEntityRowReducer(Class<E> primaryEntityType, Pair<Class<? extends BaseEntity>, JoinedEntity.JoinType>... joined) {
        this.primaryEntityType = primaryEntityType;
        this.joinedClazzListPairs = Arrays.asList(joined);
    }

    @Override
    public LinkedHashMap<Long, JoinedEntity> container() {
        return new LinkedHashMap<>();
    }

    @Override
    public void accumulate(LinkedHashMap<Long, JoinedEntity> container, RowView rowView) {
        // find out ID of primary class (it's always in the first column)
        final Long primaryEntityId = rowView.getColumn(1, Long.class);
        // find out if the given row primary entity already exists or not
        // if exists: means for primary entity there were already rows, so add another to id
        // if not exists: means it's first row for primary entity
        final JoinedEntity primaryEntity = container.computeIfAbsent(primaryEntityId, id -> new JoinedEntity(rowView.getRow(this.primaryEntityType)));

        // for every clazz that is about to get joined, try to fetch
        for (Pair<Class<? extends BaseEntity>, JoinedEntity.JoinType> joinedClazz : this.joinedClazzListPairs) {
            final BaseEntity joined = rowView.getRow(joinedClazz.getLeft());
            final JoinedEntity.JoinType joinType = joinedClazz.getRight();

            // don't want to accumulate null entities
            if (joined == null || !joined.isPersistent()) continue;

            // add to joined entity model
            primaryEntity.addJoinedEntity(joined, joinType);
        }
    }

    @Override
    public Stream<JoinedEntity> stream(LinkedHashMap<Long, JoinedEntity> container) {
        return container.values().stream();
    }


}
