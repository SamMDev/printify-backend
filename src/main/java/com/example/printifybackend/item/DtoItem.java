package com.example.printifybackend.item;

import lombok.*;

import java.math.BigDecimal;

@Builder
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
