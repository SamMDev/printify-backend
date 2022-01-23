package com.example.coderamabackend.binary_obj;

import com.example.coderamabackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DaoBinaryObject extends BaseDao<EntityBinaryObject> {

    @Autowired
    public DaoBinaryObject(Jdbi jdbi) {
        super(EntityBinaryObject.class, jdbi);
    }

}
