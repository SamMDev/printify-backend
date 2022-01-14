package com.example.coderamabackend.jdbi;

import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JoinedEntityRowReducer implements RowReducer<Map<Long, JoinedEntity>, JoinedEntity> {

    private final Class<? extends BaseEntity> primaryClazz;

    private final String primaryAlias;
    private final List<Join> joinClazzList;

    // Boolean - if true then onetomany else onetoone
    public JoinedEntityRowReducer(Class<? extends BaseEntity> primaryClazz, String primaryAlias, Join... joinClazzList) {
        this.primaryClazz = primaryClazz;
        this.primaryAlias = primaryAlias;
        this.joinClazzList = Arrays.asList(joinClazzList);
    }

    @Override
    public void accumulate(Map<Long, JoinedEntity> container, RowView rowView) {
        final JoinedEntity entity = container.computeIfAbsent(rowView.getColumn(1, Long.class),
                id -> new JoinedEntity(rowView.getRow(this.primaryClazz), this.primaryAlias, this.joinClazzList));
        this.joinClazzList.forEach(join -> {
            final String alias = join.getAlias();
            if (join.isOneToMany() || !entity.containsKey(alias)) {
                BaseEntity baseEntity = rowView.getRow(join.getEntityClass());
                if(baseEntity != null && baseEntity.getId() != null)
                    entity.addEntity(baseEntity, alias, join.isOneToMany());
            }
        });
    }

    @Override
    public Map<Long, JoinedEntity> container() {
        return new LinkedHashMap<>();
    }

    @Override
    public Stream<JoinedEntity> stream(Map<Long, JoinedEntity> container) {
        return container.values().stream();
    }

}
