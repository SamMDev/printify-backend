package com.example.coderamabackend.user;

import com.example.coderamabackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DaoUser extends BaseDao<EntityUser> {

    private final Jdbi jdbi;

    @Autowired
    public DaoUser(Jdbi jdbi) {
        super(EntityUser.class, jdbi);
        this.jdbi = jdbi;
    }

    /**
     * Fids user by given username
     * @param username  given username
     * @return          found user
     */
    public EntityUser findByUsername(String username) {
        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT * FROM printify.user WHERE username = :username")
                                .bind("username", username)
                                .mapToBean(EntityUser.class)
                                .first()
        );
    }
}