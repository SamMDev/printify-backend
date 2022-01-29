package com.example.printifybackend.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoFetchOrder {
    @JsonProperty("id")
    private String uuid;
    @JsonProperty("amount")
    private Long amount;
}
