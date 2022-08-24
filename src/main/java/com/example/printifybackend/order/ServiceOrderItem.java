package com.example.printifybackend.order;

import com.example.printifybackend.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOrderItem extends AbstractEntityService<EntityOrderItem, DaoOrderItem> {

    @Autowired
    public ServiceOrderItem(DaoOrderItem dao) {
        super(dao);
    }

}
