package com.example.coderamabackend.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoItem {
    private String name;
    private String uuid;
    private String description;
    private String dimensions;
    private BigDecimal price;
    private byte[] data;
}
