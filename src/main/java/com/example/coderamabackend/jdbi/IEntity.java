package com.example.coderamabackend.jdbi;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@MappedSuperclass
public interface IEntity extends Serializable, Comparable<IEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long getId();

    default boolean isPersistent() {
        return this.getId() != null;
    }

    default boolean isTransient() {
        return this.getId() == null;
    }

    default int compareTo(IEntity o) {
        if (o==null) return 1;
        if (this.getId() == null && o.getId() == null) return 0;
        return Objects.compare(this, o, Comparator.comparingLong(IEntity::getId));
    }
}
