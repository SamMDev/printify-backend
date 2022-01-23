package com.example.coderamabackend.order;

import com.example.coderamabackend.DtoConverter;
import com.example.coderamabackend.order.dto.DtoRequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServiceOrder {

    private final DaoOrder daoOrder;
    @Autowired
    public ServiceOrder(DaoOrder daoOrder) {
        this.daoOrder = daoOrder;
    }

    public void saveOrder(DtoRequestOrder order) {
        EntityOrder entityOrder = DtoConverter.convert(order, EntityOrder.class);
        if (entityOrder == null) return;
        entityOrder.setDate(LocalDate.now());

        this.daoOrder.insert(entityOrder);
    }

}
