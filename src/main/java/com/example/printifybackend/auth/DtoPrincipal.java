package com.example.printifybackend.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter @Setter
public class DtoPrincipal {
    private String username;
    private Set<String> roles;
    private Set<String> privileges;
}
