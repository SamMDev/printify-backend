package com.example.printifybackend.order;

import com.example.printifybackend.order.dto.DtoOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ControllerOrder {

    private final ServiceOrder serviceOrder;

    @PostMapping("/create")
    public void createOrder(@RequestBody DtoOrder order) {
        this.serviceOrder.createOrder(order);
    }

}
