package com.example.printifybackend.contact_into;

import com.example.printifybackend.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceContactInfo extends AbstractEntityService<EntityContactInfo, DaoContactInfo> {


    @Autowired
    protected ServiceContactInfo(DaoContactInfo dao) {
        super(dao);
    }
}
