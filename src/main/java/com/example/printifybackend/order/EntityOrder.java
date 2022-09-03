package com.example.printifybackend.order;

import com.example.printifybackend.contact_into.EntityContactInfo;
import com.example.printifybackend.jdbi.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "order", schema = "printify")
@NoArgsConstructor @AllArgsConstructor
public class EntityOrder extends BaseEntity {

    @ManyToOne(targetEntity = EntityContactInfo.class)
    @Column(name = "contact_info_id", nullable = false)
    private Long contactInfoId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "pay_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPayStatus payStatus;

    @Column(name = "additional_info", length = 1000)
    private String additionalInfo;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
