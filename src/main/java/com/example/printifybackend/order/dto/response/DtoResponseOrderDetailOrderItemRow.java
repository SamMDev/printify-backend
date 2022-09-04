package com.example.printifybackend.order.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class DtoResponseOrderDetailOrderItemRow {
    private Integer amount;
    private BigDecimal price;
    private DtoDetailItemRow item;

    @Getter @Setter
    public static class DtoDetailItemRow {
        private String uuid;
        private String name;
        private String description;
    }
}
