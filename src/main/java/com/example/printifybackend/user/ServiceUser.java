package com.example.printifybackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceUser {

    private final DaoUser daoUser;
    private final PasswordEncoder passwordEncoder;

    public List<EntityUser> getUsers() {
        return this.daoUser.findAll();
    }

    public void save(EntityUser user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.daoUser.insert(user);
    }
}
