package com.example.printifybackend.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Privileges implements GrantedAuthority {
    MANAGE_USERS,
    ADD_PRODUCTS,
    MANAGE_ORDERS,
    SHOW_ORDERS;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
