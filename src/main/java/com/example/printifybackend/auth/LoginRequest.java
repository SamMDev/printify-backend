package com.example.printifybackend.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
}
