package com.example.coderamabackend.order;

import com.example.coderamabackend.Converter;
import com.example.coderamabackend.auth.Roles;
import com.example.coderamabackend.order.dto.DtoOrder;
import com.example.coderamabackend.order.dto.DtoRequestOrder;
import com.example.coderamabackend.user.DaoUser;
import com.example.coderamabackend.user.EntityUser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

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

}
