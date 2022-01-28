package com.example.coderamabackend.user;


import com.example.coderamabackend.Converter;
import com.example.coderamabackend.auth.Privileges;
import com.example.coderamabackend.auth.Roles;
import com.example.coderamabackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Entity
@Table(name = "user", schema = "printify")
public class EntityUser extends BaseEntity {

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "roles", length = 200)
    private String roles;

    @Column(name = "privileges", length = 200)
    private String privileges;

    /**
     * Users roles will be stored in json list format
     * means for example roles MANUFACTURE, FINANCE will be as ["MANUFACTURE", "FINANCE"]
     *
     * @return set of user roles deserialized
     */
    public Set<Roles> deserializeUserRoles() {
        if (StringUtils.isBlank(this.roles)) return Collections.emptySet();
        return Arrays.stream(Converter.read(this.roles, String[].class))
                .map(Roles::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Users privileges will be stored in json list format
     * means for example privileges MANAGE_USERS, ADD_PRODUCTS will be as ["MANAGE_USERS", "ADD_PRODUCTS"]
     *
     * @return set of user privileges deserialized
     */
    public Set<Privileges> deserializeUserPrivileges() {
        if (StringUtils.isBlank(this.privileges)) return Collections.emptySet();
        return Arrays.stream(Converter.read(this.roles, String[].class))
                .map(Privileges::valueOf)
                .collect(Collectors.toSet());
    }
}
