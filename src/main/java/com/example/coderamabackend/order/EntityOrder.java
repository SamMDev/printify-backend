package com.example.coderamabackend.order;

import com.example.coderamabackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "order", schema = "printify")
public class EntityOrder extends BaseEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postcode", length = 10)
    private String postcode;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "content", length = 10000)
    private String content;

    @Column(name = "price")
    private BigDecimal price;

}
