package com.example.printifybackend.binary_obj;

import com.example.printifybackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DaoBinaryObject extends BaseDao<EntityBinaryObject> {

    @Autowired
    public DaoBinaryObject(Jdbi jdbi) {
        super(EntityBinaryObject.class, jdbi);
    }

    @Override
    public String buildWhereStatement(Map<String, Object> filters, Map<String, Object> bind) {
        return null;
    }

    @Override
    public Long totalRowCount(Map<String, Object> filters) {
        return null;
    }
}
