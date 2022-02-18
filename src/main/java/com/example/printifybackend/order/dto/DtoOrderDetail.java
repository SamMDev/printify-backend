package com.example.printifybackend.order.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Order detail must have all the information from order,
 * but also contains list of order items of given order
 */
public class DtoOrderDetail extends DtoOrder {
    private List<DtoSingleOrderItem> orderItems;
}
