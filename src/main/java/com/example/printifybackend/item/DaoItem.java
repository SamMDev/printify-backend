package com.example.printifybackend.item;

import com.example.printifybackend.binary_obj.EntityBinaryObject;
import com.example.printifybackend.jdbi.*;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DaoItem extends BaseDao<EntityItem> {

    @Autowired
    public DaoItem(Jdbi jdbi) {
        super(EntityItem.class, jdbi);
    }

    /**
     * Finds all items with their images
     *
     * @return  joined items with images
     */
    public List<JoinedEntity> findAllWithImages() {
        return this.jdbi.withHandle(handle ->
             handle
                   .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id")
                   .reduceRows(
                           new JoinedEntityRowReducer(
                                   EntityItem.class,
                                   "item",
                                   Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                           ))
                   .collect(Collectors.toList())
        );
    }

    public List<JoinedEntity> findInternetVisibleWithImages() {
        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id WHERE item.internet_visible")
                        .reduceRows(
                                new JoinedEntityRowReducer(
                                        EntityItem.class,
                                        "item",
                                        Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                                ))
                        .collect(Collectors.toList())
        );
    }

    public JoinedEntity findByUuidWithImage(String uuid) {
        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id WHERE item.uuid = :uuid")
                        .bind("uuid", uuid)
                        .reduceRows(
                                new JoinedEntityRowReducer(
                                        EntityItem.class,
                                        "item",
                                        Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                                ))
                        .findFirst().orElse(null)
        );
    }

    public List<JoinedEntity> findByUuidsWithImages(List<String> uuids) {
        return this.jdbi.withHandle(handle ->
                handle
                        .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id WHERE item.uuid IN (<uuids>)")
                        .bindList("uuids", uuids)
                        .reduceRows(
                                new JoinedEntityRowReducer(
                                        EntityItem.class,
                                        "item",
                                        Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                                ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String buildWhereStatement(Map<String, Object> filters, Map<String, Object> bind) {
        return null;
    }

    @Override
    public Long totalRowCount(Map<String, Object> filters) {
        return null;
    }
}
