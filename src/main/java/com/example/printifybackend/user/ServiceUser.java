package com.example.printifybackend.user;

import com.example.printifybackend.auth.Privileges;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceUser {

    private final DaoUser daoUser;
    private final PasswordEncoder passwordEncoder;

    public List<EntityUser> getUsers() {
        return this.daoUser.findAll();
    }

    public Set<Privileges> getPrivilegesForUser(EntityUser user) {
        if (user == null || (StringUtils.isBlank(user.getRoles()) && StringUtils.isBlank(user.getPrivileges())))
            return Collections.emptySet();
        Set<Privileges> allPrivileges = new HashSet<>(user.deserializeUserPrivileges());
        user.deserializeUserRoles().forEach(role -> allPrivileges.addAll(role.getPrivileges()));
        return allPrivileges;
    }

    public EntityUser findByUsername(String username) {
        return this.daoUser.findByUsername(username);
    }

    public void save(EntityUser user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.daoUser.insert(user);
    }
}
