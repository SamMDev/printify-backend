package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.LazyCriteria;
import com.example.printifybackend.keyring.ServiceKeyring;
import com.example.printifybackend.keyring.dto.DtoKeyringOrder;
import com.example.printifybackend.order.dto.request.DtoRequestOrder;
import com.example.printifybackend.order.dto.response.DtoResponseOrder;
import com.example.printifybackend.order.dto.response.DtoResponseOrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ControllerOrder {

    private final ServiceOrder serviceOrder;
    private final ServiceKeyring serviceKeyring;

    @PostMapping("/create/order/product-order")
    public void createOrder(@RequestBody DtoRequestOrder order) {
        this.serviceOrder.createOrder(order);
    }

    @PostMapping("/create/order/keyring-order")
    public void createOrder(@RequestBody DtoKeyringOrder order) {
        this.serviceKeyring.createOrder(order);
    }

    @PostMapping("/get")
    public List<DtoResponseOrder> getOrders(@RequestBody LazyCriteria criteria) {
        return this.serviceOrder.getOrdersByCriteria(criteria);
    }

    @GetMapping("/get/detail/{id}")
    public DtoResponseOrderDetail getDetail(@PathVariable("id") Long orderId) {
        return this.serviceOrder.getOrderDetail(orderId);
    }

}
