package com.example.coderamabackend.rate;

import com.example.coderamabackend.budgetItem.EntityBudgetItem;
import com.example.coderamabackend.greenlight.GreenlightStatus;
import com.example.coderamabackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "rate", schema = "banker")
public class EntityRate extends BaseEntity {

    private static final long serialVersionUID = -5138316604050278916L;

    @Column(name = "price", precision = 19, scale = 5)
    private BigDecimal price;

    @Column(name = "who")
    private String who;

    @Column(name = "fromdate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate from;

    @Column(name = "todate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate to;

    @Column(name = "offeritem_id")
    @ManyToOne(targetEntity = EntityBudgetItem.class)
    private Long budgetItemId;

    @Column(name = "upgrade", length = 8000)
    private String upgrade;

    @Column(name = "greenlightstatus", length = 255)
    @Enumerated(EnumType.STRING)
    private GreenlightStatus greenlightStatus;
}
