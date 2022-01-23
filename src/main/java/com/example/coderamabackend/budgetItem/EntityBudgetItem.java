package com.example.coderamabackend.budgetItem;

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
@Table(name = "offerItem", schema = "banker")
public class EntityBudgetItem extends BaseEntity {

    @Column(name = "namefirstName")
    private String name;

    @Column(name = "manhours")
    private Integer manHours;

    @Column(name = "freetimecounting")
    private Boolean freeTimeCounting = false;

    @Column(name = "validfrom")
    private LocalDate validFrom;

    @Column(name = "validto")
    private LocalDate validTo;

    @Column(name = "price", precision = 19, scale = 5)
    private BigDecimal price;

    @Column(name = "allowedusers")
    private String allowedUsers;

    @Column(name = "deliverydate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate deliveryDate;

    @Column(name = "offer_id")
    private Long budgetId;

    @Column(name = "freehours", precision = 19, scale = 5)
    private BigDecimal freeHours;

    @Column(name = "greenlightstatus")
    @Enumerated(EnumType.STRING)
    private GreenlightStatus greenlightStatus;

    @Column(name = "upgrade", length = 2000)
    private String upgrade;

    public BigDecimal getFreeHours() {
        return this.freeHours != null ? this.freeHours : BigDecimal.ZERO;
    }

    public Integer getManHours() {
        return this.manHours == null ? 0 : this.manHours;
    }

    public BigDecimal getPrice() {
        return this.price != null ? this.price : BigDecimal.ZERO;
    }

    public BigDecimal getPriceMd() {
        return this.getPrice().multiply(BigDecimal.valueOf(8L));
    }
}
