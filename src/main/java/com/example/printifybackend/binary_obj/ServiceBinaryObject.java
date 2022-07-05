package com.example.printifybackend.binary_obj;

import com.example.printifybackend.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceBinaryObject extends AbstractEntityService<EntityBinaryObject, DaoBinaryObject> {

    @Autowired
    public ServiceBinaryObject(DaoBinaryObject daoBinaryObject) {
        super(daoBinaryObject);
    }

}
