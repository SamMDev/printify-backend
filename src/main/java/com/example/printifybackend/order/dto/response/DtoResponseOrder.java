package com.example.printifybackend.order.dto.response;

import com.example.printifybackend.order.OrderPayStatus;
import com.example.printifybackend.order.OrderStatus;
import com.example.printifybackend.order.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// to be shown in row in table of all orders
public record DtoResponseOrder(
        BigDecimal price,
        OrderStatus status,
        OrderPayStatus payStatus,
        OrderType orderType,
        String additionalInfo,
        LocalDateTime created
) {
}
