package com.example.coderamabackend.item;

import com.example.coderamabackend.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceItem {

    private final DaoItem daoItem;
    @Autowired
    public ServiceItem(DaoItem daoItem) {
        this.daoItem = daoItem;
    }

    public DtoItem findByUuidWithImage(String uuid) {
        return DtoConverter.convertJoined(this.daoItem.findByUuidWithImage(uuid), DtoItem.class);
    }

    public List<DtoItem> findAllWithImages() {
        return this.daoItem.findAllWithImages()
                .stream()
                .map(e -> DtoConverter.convertJoined(e, DtoItem.class))
                .collect(Collectors.toList());
    }

    public List<DtoItem> findByUuidsWithImages(List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) return Collections.emptyList();
        return this.daoItem.findByUuidsWithImages(uuids)
                .stream()
                .map(e -> DtoConverter.convertJoined(e, DtoItem.class))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        this.daoItem.deleteById(id);
    }
}