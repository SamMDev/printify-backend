package com.example.printifybackend.item;

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

    @GetMapping("/internet-visible")
    public List<DtoItem> findInternetVisible() {
        return this.serviceItem.findInternetVisibleWithImages();
    }

    @GetMapping("/internet-visible/searchBy/{search}")
    public List<DtoItem> findInternetVisibleBySearch(@PathVariable("search") String search) {
        return this.serviceItem.findInternetVisibleWithImages(search);
    }

    @GetMapping("/uuid/{uuid}")
    public DtoItem findByUuid(@PathVariable("uuid") String uuid) {
        return this.serviceItem.findByUuidWithImage(uuid);
    }

    @PostMapping("/uuid")
    public List<DtoItem> findByUuids(@RequestBody List<String> uuids) {
        return this.serviceItem.findByUuidsWithImages(uuids);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.serviceItem.deleteById(id);
    }

    @PutMapping("/update/visibility")
    public void changeVisibility(@RequestParam("uuid") String uuid, @RequestParam("visible") boolean visible) {
        this.serviceItem.changeVisibility(uuid, visible);
    }

}
