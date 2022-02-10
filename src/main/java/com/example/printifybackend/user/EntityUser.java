package com.example.printifybackend.user;


import com.example.printifybackend.Converter;
import com.example.printifybackend.auth.Privilege;
import com.example.printifybackend.auth.Role;
import com.example.printifybackend.jdbi.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    public Set<Role> deserializeUserRoles() {
        if (StringUtils.isBlank(this.roles)) return Collections.emptySet();
        return Arrays.stream(Converter.read(this.roles, String[].class))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Users privileges will be stored in json list format
     * means for example privileges MANAGE_USERS, ADD_PRODUCTS will be as ["MANAGE_USERS", "ADD_PRODUCTS"]
     *
     * @return set of user privileges deserialized
     */
    public Set<Privilege> deserializeUserPrivileges() {
        if (StringUtils.isBlank(this.privileges)) return Collections.emptySet();
        return Arrays.stream(Converter.read(this.privileges, String[].class))
                .map(Privilege::valueOf)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all the privileges user has
     * @return  privileges
     */
    public Set<Privilege> getAllPrivileges() {
        final Set<Privilege> privileges = new HashSet<>(this.deserializeUserPrivileges());
        for (Role role : this.deserializeUserRoles()) {
            privileges.addAll(role.getPrivileges());
        }
        return privileges;
    }
}
