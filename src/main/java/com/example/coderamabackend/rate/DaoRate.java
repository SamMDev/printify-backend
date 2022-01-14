package com.example.coderamabackend.rate;

import com.example.coderamabackend.jdbi.AbstractDao;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaoRate extends AbstractDao<EntityRate> {

    @SqlQuery("SELECT * FROM banker.rate LIMIT 1")
    Optional<EntityRate> findFirst();

    @SqlQuery("SELECT * FROM banker.rate LIMIT :limit OFFSET :offset")
    List<EntityRate> getDemoData(
            @Bind("offset") Long offset,
            @Bind("limit") Long limit
    );

}
