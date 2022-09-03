package com.example.printifybackend.order.dto.request;

import com.example.printifybackend.contact_into.DtoContactInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DtoRequestOrder {
    private DtoContactInfo contactInfo;
    private List<DtoOrderItem> items;
}
