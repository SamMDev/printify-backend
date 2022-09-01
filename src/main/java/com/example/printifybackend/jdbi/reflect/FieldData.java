package com.example.printifybackend.jdbi.reflect;

import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Field;

@Getter
public class FieldData {
    private final Field field;
    private final String fieldName;

    public FieldData(@NonNull Field field) {
        this.field = field;
        this.fieldName = field.getName();
    }
}
