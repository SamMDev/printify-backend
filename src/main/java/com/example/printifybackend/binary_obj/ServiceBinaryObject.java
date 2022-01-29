package com.example.printifybackend.binary_obj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceBinaryObject {

    private final DaoBinaryObject daoBinaryObject;
    @Autowired
    public ServiceBinaryObject(DaoBinaryObject daoBinaryObject) {
        this.daoBinaryObject = daoBinaryObject;
    }

}
