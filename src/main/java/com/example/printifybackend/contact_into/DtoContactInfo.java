package com.example.printifybackend.contact_into;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DtoContactInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipCode;
}
