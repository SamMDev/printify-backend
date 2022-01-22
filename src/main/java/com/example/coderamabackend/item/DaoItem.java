package com.example.coderamabackend.item;

import com.example.coderamabackend.binary_obj.EntityBinaryObject;
import com.example.coderamabackend.jdbi.AbstractDao;
import com.example.coderamabackend.jdbi.Join;
import com.example.coderamabackend.jdbi.JoinedEntity;
import com.example.coderamabackend.jdbi.JoinedEntityRowReducer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface DaoItem extends AbstractDao<EntityItem> {

    /**
     * Finds all items with their images
     *
     * @return  joined items with images
     */
    default List<JoinedEntity> findAllWithImages() {
        return getHandle()
                .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id")
                .reduceRows(
                        new JoinedEntityRowReducer(
                                EntityItem.class,
                                "item",
                                Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                )).collect(Collectors.toList());
    }

    /**
     * Finds item by uuid joined with its image
     *
     * @param uuid  wanted uuid
     * @return      joined with image
     */
    default JoinedEntity findByUuidWithImage(String uuid) {
        return getHandle()
                .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id WHERE item.uuid = :uuid")
                .bind("uuid", uuid)
                .reduceRows(
                        new JoinedEntityRowReducer(
                                EntityItem.class,
                                "item",
                                Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                        ))
                .findFirst().orElse(null);
    }

    default List<JoinedEntity> findByUuidsWithImages(List<String> uuids) {
        return getHandle()
                .createQuery("SELECT * FROM printify.item LEFT OUTER JOIN printify.binary_obj ON item.image_id = binary_obj.id WHERE item.uuid IN (<uuids>)")
                .bindList("uuids", uuids)
                .reduceRows(
                        new JoinedEntityRowReducer(
                                EntityItem.class,
                                "item",
                                Join.oneToOneEntity(EntityBinaryObject.class, "binary_obj")
                        ))
                .collect(Collectors.toList());
    }
}
