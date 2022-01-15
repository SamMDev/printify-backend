package com.example.coderamabackend.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = "*")
public class ControllerItem {

    private final ServiceItem serviceItem;
    @Autowired
    public ControllerItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    @GetMapping("/all")
    public List<DtoItem> findAll() {
        return this.serviceItem.findAllWithImages();
    }

    @GetMapping("/uuid/{uuid}")
    public DtoItem findByUuid(@PathVariable("uuid") String uuid) {
        return this.serviceItem.findByUuid(uuid);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.serviceItem.deleteById(id);
    }

}
