package com.example.printifybackend.order.dto;

import com.example.printifybackend.item.DtoItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
/**
 * Single order item in order.
 * Has all the item information, but also has amount and price for that amount
 */
public class DtoSingleOrderItem extends DtoItem {
    private Integer amount;
    private BigDecimal price;
}
