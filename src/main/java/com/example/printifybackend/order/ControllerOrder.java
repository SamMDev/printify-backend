package com.example.printifybackend.order;

import com.example.printifybackend.order.dto.DtoOrder;
import com.example.printifybackend.order.dto.DtoRequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class ControllerOrder {

    private final ServiceOrder serviceOrder;
    @Autowired
    public ControllerOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    @GetMapping("/id/{id}")
    public DtoOrder getById(@PathVariable("id") Long id) {
        return this.serviceOrder.getById(id);
    }

    @PostMapping("/save")
    public void saveOrder(@RequestBody DtoRequestOrder order) {
        this.serviceOrder.saveOrder(order);
    }
}
