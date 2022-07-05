package com.example.printifybackend.item;

import com.example.printifybackend.AbstractEntityService;
import com.example.printifybackend.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceItem extends AbstractEntityService<EntityItem, DaoItem> {

    @Autowired
    public ServiceItem(DaoItem daoItem) {
        super(daoItem);
    }

    public DtoItem findByUuidWithImage(String uuid) {
        return Converter.convertJoined(this.dao.findByUuidWithImage(uuid), DtoItem.class);
    }

    public List<DtoItem> findAllWithImages() {
        return this.dao.findAllWithImages()
                .stream()
                .map(e -> Converter.convertJoined(e, DtoItem.class))
                .toList();
    }

    public List<DtoItem> findInternetVisibleWithImages() {
        return this.dao.findInternetVisibleWithImages()
                .stream()
                .map(e -> Converter.convertJoined(e, DtoItem.class))
                .toList();
    }

    public List<DtoItem> findInternetVisibleWithImages(String searchBy) {
        return this.dao.findInternetVisibleWithImages(searchBy)
                .stream()
                .map(e -> Converter.convertJoined(e, DtoItem.class))
                .toList();
    }

    public List<DtoItem> findByUuidsWithImages(List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) return Collections.emptyList();
        return this.dao.findByUuidsWithImages(uuids)
                .stream()
                .map(e -> Converter.convertJoined(e, DtoItem.class))
                .toList();
    }

    public void deleteById(Long id) {
        this.dao.deleteById(id);
    }
}
