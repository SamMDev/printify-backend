package com.example.printifybackend.user;

import lombok.Data;

@Data
public class DtoUser {
    private String username;
    private String password;
    private String roles;
    private String privileges;
}
