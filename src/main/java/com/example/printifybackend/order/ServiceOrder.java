package com.example.printifybackend.order;

import com.example.printifybackend.Converter;
import com.example.printifybackend.jdbi.Criteria;
import com.example.printifybackend.order.dto.DtoOrder;
import com.example.printifybackend.order.dto.DtoRequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOrder {

    private final DaoOrder daoOrder;
    @Autowired
    public ServiceOrder(DaoOrder daoOrder) {
        this.daoOrder = daoOrder;
    }

    public void saveOrder(DtoRequestOrder order) {
        EntityOrder entityOrder = Converter.convert(order, EntityOrder.class);
        if (entityOrder == null) return;
        entityOrder.setDate(LocalDateTime.now());

        this.daoOrder.insert(entityOrder);
    }

    public DtoOrder getById(Long id) {
        EntityOrder order = this.daoOrder.findById(id);
        if (order == null) return null;
        return Converter.convert(order, DtoOrder.class);
    }

    public List<DtoOrder> getWithCriteria(Criteria criteria) {
        return this.daoOrder.getOrdersWithCriteria(criteria)
                .stream()
                .map(o -> Converter.convert(o, DtoOrder.class))
                .collect(Collectors.toList());
    }

}
