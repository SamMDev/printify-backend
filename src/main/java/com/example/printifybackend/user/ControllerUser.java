package com.example.printifybackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ControllerUser {

    private final ServiceUser serviceUser;

    @GetMapping("/all")
    public ResponseEntity<List<EntityUser>> getUsers() {
        return ResponseEntity.ok().body(this.serviceUser.getUsers());
    }

}
