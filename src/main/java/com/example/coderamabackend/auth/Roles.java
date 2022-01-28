package com.example.coderamabackend.auth;

import lombok.Getter;

import java.util.Set;

public enum Roles {

    FULL_ADMIN(Set.of(Privileges.values())),
    MANUFACTURER(Set.of(Privileges.ADD_PRODUCTS)),
    WEB_DEVELOPER(Set.of(Privileges.SHOW_ORDERS, Privileges.ADD_PRODUCTS)),
    MARKETING(Set.of(Privileges.ADD_PRODUCTS, Privileges.SHOW_ORDERS)),
    FINANCE(Set.of(Privileges.MANAGE_ORDERS,  Privileges.SHOW_ORDERS, Privileges.ADD_PRODUCTS)),
    COACH(Set.of(Privileges.SHOW_ORDERS, Privileges.MANAGE_ORDERS));

    @Getter
    private Set<Privileges> privileges;

    Roles(Set<Privileges> assignedPrivileges) {
        this.privileges = assignedPrivileges;
    }
}
