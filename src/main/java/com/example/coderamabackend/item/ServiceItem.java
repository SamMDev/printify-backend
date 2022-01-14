package com.example.coderamabackend.item;

import com.example.coderamabackend.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceItem {

    private final DaoItem daoItem;
    @Autowired
    public ServiceItem(DaoItem daoItem) {
        this.daoItem = daoItem;
    }

    public List<DtoItem> findAll() {
        return this.daoItem.findAll()
                .stream()
                .map(e -> DtoConverter.convert(e, DtoItem.class))
                .collect(Collectors.toList());
    }

    public List<DtoItem> findAllWithImages() {
        return this.daoItem.findAllWithImages()
                .stream()
                .map(e -> DtoConverter.convertJoined(e, DtoItem.class))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        this.daoItem.deleteById(id);
    }
}
