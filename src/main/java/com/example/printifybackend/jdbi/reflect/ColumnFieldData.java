package com.example.printifybackend.jdbi.reflect;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Optional;

@Getter
public class ColumnFieldData extends FieldData {
    private final String columnName;
    private final boolean isId;

    public ColumnFieldData(Field field) {
        super(field);
        this.isId = field.getAnnotation(Id.class) != null;
        this.columnName = this.isId
                ? "id"
                : Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::name)
                .orElseThrow(() -> new EntityReflectionManager.EntityReflectionException(
                        String.format("Trying to instantiate ColumnFieldData class with field %s that is not annotated with @Column annotation", field)
                ));
    }
}
