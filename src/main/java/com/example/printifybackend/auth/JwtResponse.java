package com.example.printifybackend.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private Set<String> privileges;

    public JwtResponse(String accessToken, String username, Set<String> privileges) {
        this.accessToken = accessToken;
        this.username = username;
        this.privileges = privileges;
    }
}
