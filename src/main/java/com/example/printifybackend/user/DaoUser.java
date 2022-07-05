package com.example.printifybackend.user;

import com.example.printifybackend.jdbi.BaseDao;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DaoUser extends BaseDao<EntityUser> {

    @Autowired
    public DaoUser(Jdbi jdbi) {
        super(EntityUser.class, jdbi);
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

    public Boolean existsByUsername(String username) {
        return this.jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT EXISTS (SELECT 1 FROM printify.user WHERE username = :username)")
                                .bind("username", username)
                                .mapTo(Boolean.class)
                                .first()
        );
    }

}
