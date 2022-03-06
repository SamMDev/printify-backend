package com.example.printifybackend.order;

import com.example.printifybackend.jdbi.LazyCriteria;
import com.example.printifybackend.order.dto.DtoOrder;
import com.example.printifybackend.order.dto.DtoOrderDetail;
import com.example.printifybackend.order.dto.DtoRequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class ControllerOrder {

    private final ServiceOrder serviceOrder;
    @Autowired
    public ControllerOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    @PostMapping("/id/{id}")
    public DtoOrderDetail getById(@PathVariable("id") Long id) {
        return this.serviceOrder.getDetailById(id);
    }

    @PostMapping("/finalize/id/{id}")
    public void finalizeById(@PathVariable("id") Long id) {
        this.serviceOrder.editFinalization(id, true);
    }

    @PostMapping("/un-finalize/id/{id}")
    public void unFinalizeById(@PathVariable("id") Long id) {
        this.serviceOrder.editFinalization(id, false);
    }

    @PostMapping("/get")
    public ResponseEntity<List<DtoOrder>> getWithCriteria(@RequestBody LazyCriteria lazyCriteria) {
        return ResponseEntity.ok(this.serviceOrder.getWithCriteria(lazyCriteria));
    }

    @PostMapping("/count")
    public Long countWithCriteria(@RequestBody LinkedHashMap<String, Object> filter) {
        return this.serviceOrder.count(filter);
    }

    @PostMapping("/save")
    public void saveOrder(@RequestBody DtoRequestOrder order) {
        this.serviceOrder.saveOrder(order);
    }
}
