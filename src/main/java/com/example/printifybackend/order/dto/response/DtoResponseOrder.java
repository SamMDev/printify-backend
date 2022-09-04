package com.example.printifybackend.order.dto.response;

import com.example.printifybackend.order.OrderPayStatus;
import com.example.printifybackend.order.OrderStatus;
import com.example.printifybackend.order.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
// to be shown in row in table of all orders
public class DtoResponseOrder {
    private Long id;
    private BigDecimal price;
    private OrderStatus status;
    private OrderPayStatus payStatus;
    private OrderType orderType;
    private String additionalInfo;
    private LocalDateTime created;
}
