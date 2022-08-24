package com.example.printifybackend.order;

import com.example.printifybackend.contact_into.EntityContactInfo;
import com.example.printifybackend.jdbi.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Entity
@Table(name = "order", schema = "printify")
public class EntityOrder extends BaseEntity {

    @ManyToOne(targetEntity = EntityContactInfo.class)
    @Column(name = "contact_info", nullable = false)
    private Long contactInfo;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "pay_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPayStatus payStatus;
}
