package com.example.printifybackend.user;

import com.example.printifybackend.Converter;
import com.example.printifybackend.auth.Privilege;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceUser {

    private final DaoUser daoUser;
    private final PasswordEncoder passwordEncoder;

    public List<DtoUser> getUsers() {
        return this.daoUser.findAll().stream().map(user -> Converter.convert(user, DtoUser.class)).collect(Collectors.toList());
    }

    public Set<Privilege> getPrivilegesForUser(EntityUser user) {
        if (user == null || (StringUtils.isBlank(user.getRoles()) && StringUtils.isBlank(user.getPrivileges())))
            return Collections.emptySet();
        Set<Privilege> allPrivileges = new HashSet<>(user.deserializeUserPrivileges());
        user.deserializeUserRoles().forEach(role -> allPrivileges.addAll(role.getPrivileges()));
        return allPrivileges;
    }

    public void saveNewUser(String username, String rawPassword) {
        EntityUser user = new EntityUser();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(rawPassword));
        this.save(user);
    }

    public EntityUser findByUsername(String username) {
        return this.daoUser.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return this.daoUser.existsByUsername(username);
    }

    public void save(EntityUser user) {
        this.daoUser.insert(user);
    }
}
