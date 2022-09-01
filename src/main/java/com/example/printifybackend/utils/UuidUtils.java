package com.example.printifybackend.utils;

import java.util.UUID;

public class UuidUtils {

    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
