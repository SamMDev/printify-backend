package com.example.coderamabackend.rate;

import com.example.coderamabackend.rate.dto.DtoRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rate")
@CrossOrigin(origins = "*")
public class ControllerRate {

    private final ServiceRate serviceRate;

    @Autowired
    public ControllerRate(ServiceRate serviceRate) {
        this.serviceRate = serviceRate;
    }

    @GetMapping("/id/{id}")
    public DtoRate getById(@PathVariable("id") Long id) {
        return this.serviceRate.findById(id);
    }

    @GetMapping("/demo")
    public List<DtoRate> getDemoData() {
        return this.serviceRate.getDemoData();
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.serviceRate.deleteById(id);
    }

}
