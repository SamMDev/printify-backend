package com.example.printifybackend.item;


import com.example.printifybackend.binary_obj.EntityBinaryObject;
import com.example.printifybackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "item", schema = "printify")
public class EntityItem extends BaseEntity {

    @Column(name = "uuid", length = 36)
    private String uuid;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "dimensions", length = 1000)
    private String dimensions;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image_id")
    @ManyToOne(targetEntity = EntityBinaryObject.class)
    private Long imageId;

    @Column(name = "file_id")
    @ManyToOne(targetEntity = EntityBinaryObject.class)
    private Long fileId;

    @Column(name = "internet_visible")
    private Boolean internetVisible;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItemType type;
}
