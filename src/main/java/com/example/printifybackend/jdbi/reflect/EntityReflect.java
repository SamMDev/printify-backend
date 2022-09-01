package com.example.printifybackend.jdbi.reflect;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class EntityReflect<E> {

    private final String tableName;
    private final Class<E> clazz;

    private final List<FieldData> fieldsData = new ArrayList<>();
    private final List<ColumnFieldData> columnFieldsData = new ArrayList<>();
    private final List<ColumnFieldData> columnFieldsDataWithoutId = new ArrayList<>();

    private final List<Field> fields = new ArrayList<>();

    private final List<String> fieldNamesWithoutId = new ArrayList<>();
    private final List<String> columnNames = new ArrayList<>();
    private final List<String> columnNamesWithoutId = new ArrayList<>();

    public EntityReflect(Class<E> clazz) {

        this.clazz = clazz;
        this.tableName =
                Optional.ofNullable(
                        Optional.ofNullable(this.clazz.getAnnotation(Table.class))
                                .orElseThrow(() -> new EntityReflectionManager.EntityReflectionException(String.format("Could not create entity reflection of %s, class is not annotated with @Table annotation", clazz.getName())))
                ).map(Table::name).orElseThrow(() -> new EntityReflectionManager.EntityReflectionException(String.format("Could not create entity reflection of %s, class @Table annotation does not have table name", clazz.getName())));

        // loop through fields and save all the needed info
        for (Field field : EntityReflectionManager.getAllFields(clazz)) {
            // save every field
            this.fields.add(field);
            this.fieldsData.add(new FieldData(field));

            // save column annotated fields (also field annotated with @Id is mapped field)
            if (field.getAnnotation(Column.class) != null || field.getAnnotation(Id.class) != null) {

                this.columnNames.add(field.getName());
                this.columnFieldsData.add(new ColumnFieldData(field));

                // instead of ID field
                if (field.getAnnotation(Id.class) == null) {
                    this.fieldNamesWithoutId.add(field.getName());
                    this.columnNamesWithoutId.add(field.getAnnotation(Column.class).name());
                    this.columnFieldsDataWithoutId.add(new ColumnFieldData(field));
                }
            }
        }
    }

    /**
     * Finds field annotated by Column annotation with given name
     *
     * @param name given name
     * @return field
     */
    public Field getColumnAnnotatedFieldByName(String name) {
        if (name == null) return null;
        return this.columnFieldsData.stream()
                .filter(fieldData -> fieldData.getColumnName().equalsIgnoreCase(name))
                .findFirst().map(ColumnFieldData::getField).orElse(null);
    }

    /**
     * Creates instance if reflected clazz
     *
     * @return instance object
     */
    public E getInstance() {
        if (this.getClazz() == null) return null;
        try {
            return this.getClazz().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new EntityReflectionManager.EntityReflectionException(
                    e, String.format("Could not create instance of %s entity, constructor could not be accessed", this.getClazz().getName())
            );
        }
    }

    /**
     * Having an object and its field, sets value
     *
     * @param obj   object we want to set field
     * @param field field to be set
     * @param value new field value
     */
    public void setFieldValue(E obj, Field field, Object value) {
        if (obj == null)
            throw new EntityReflectionManager.EntityReflectionException("Could not set field value to entity. Provided entity is null");
        if (field == null)
            throw new EntityReflectionManager.EntityReflectionException("Could not set field value to entity. Provided field is null");

        boolean accessibility = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new EntityReflectionManager.EntityReflectionException(e, "Could not set field value");
        } finally {
            field.setAccessible(accessibility);
        }
    }
}