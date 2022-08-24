package com.example.printifybackend.order;

import com.example.printifybackend.item.EntityItem;
import com.example.printifybackend.jdbi.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_item", schema = "printify")
@NoArgsConstructor @AllArgsConstructor
public class EntityOrderItem extends BaseEntity {

    @ManyToOne(targetEntity = EntityItem.class)
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @ManyToOne(targetEntity = EntityOrder.class)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
