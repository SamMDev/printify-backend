package com.example.coderamabackend.rate;

import com.example.coderamabackend.DtoConverter;
import com.example.coderamabackend.rate.dto.DtoRate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ServiceRate {

    private final DaoRate daoRate;

    @Autowired
    public ServiceRate(DaoRate daoRate, ObjectMapper objectMapper) {
        this.daoRate = daoRate;
    }

    public List<DtoRate> getDemoData() {
        return this.daoRate.getDemoData(0L, 20L)
                .stream()
                .map(e -> DtoConverter.convert(e, DtoRate.class))
                .collect(Collectors.toList());
    }

    // DAO forwards
    public DtoRate findById(Long id) {
        return DtoConverter.convert(this.daoRate.findById(id), DtoRate.class);
    }

    public void deleteById(Long id) {
        this.daoRate.deleteById(id);
    }
}
