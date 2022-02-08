package com.example.printifybackend.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequest {
    private String username;
    private String password;
}
