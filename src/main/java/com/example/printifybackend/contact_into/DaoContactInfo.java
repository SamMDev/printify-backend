package com.example.printifybackend.contact_into;

import com.example.printifybackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DaoContactInfo extends BaseDao<EntityContactInfo> {

    @Autowired
    public DaoContactInfo(Jdbi jdbi) {
        super(EntityContactInfo.class, jdbi);
    }
}
