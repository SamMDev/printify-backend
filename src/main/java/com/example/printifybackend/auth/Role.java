package com.example.printifybackend.auth;

import lombok.Getter;

import java.util.Set;

public enum Role {

    FULL_ADMIN(Set.of(Privilege.values())),
    MANUFACTURER(Set.of(Privilege.ADD_PRODUCTS)),
    WEB_DEVELOPER(Set.of(Privilege.SHOW_ORDERS, Privilege.ADD_PRODUCTS)),
    MARKETING(Set.of(Privilege.ADD_PRODUCTS, Privilege.SHOW_ORDERS)),
    FINANCE(Set.of(Privilege.MANAGE_ORDERS,  Privilege.SHOW_ORDERS, Privilege.ADD_PRODUCTS)),
    COACH(Set.of(Privilege.SHOW_ORDERS, Privilege.MANAGE_ORDERS));

    @Getter
    private Set<Privilege> privileges;

    Role(Set<Privilege> assignedPrivileges) {
        this.privileges = assignedPrivileges;
    }
}
