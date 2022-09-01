package com.example.printifybackend.keyring.dto;

import com.example.printifybackend.contact_into.DtoContactInfo;

public record DtoKeyringOrder(DtoContactInfo contactInfo, DtoItemKeyring keyringItem) {
}