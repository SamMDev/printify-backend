package com.example.printifybackend.user;

import lombok.Data;

@Data
public class DtoUser {
    private String username;
    private String password;
    // serialized roles
    private String roles;
    // serialized privileges
    private String privileges;
}
