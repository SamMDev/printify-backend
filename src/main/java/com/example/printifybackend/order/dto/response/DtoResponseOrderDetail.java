package com.example.printifybackend.order.dto.response;

import com.example.printifybackend.contact_into.DtoContactInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DtoResponseOrderDetail extends DtoResponseOrder {
    private DtoContactInfo contactInfo;
    private List<DtoResponseOrderDetailOrderItemRow> orderItems;
}
