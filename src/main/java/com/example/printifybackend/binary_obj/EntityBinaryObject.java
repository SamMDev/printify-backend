package com.example.printifybackend.binary_obj;

import com.example.printifybackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "binary_obj", schema = "printify")
public class EntityBinaryObject extends BaseEntity {

    @Column(name = "data")
    private byte[] data;

}
