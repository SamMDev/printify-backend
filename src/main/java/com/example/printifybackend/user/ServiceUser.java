package com.example.printifybackend.user;

import com.example.printifybackend.AbstractEntityService;
import com.example.printifybackend.Converter;
import com.example.printifybackend.auth.Privilege;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceUser extends AbstractEntityService<EntityUser, DaoUser> {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServiceUser(DaoUser dao, PasswordEncoder passwordEncoder) {
        super(dao);
        this.passwordEncoder = passwordEncoder;
    }


    public List<DtoUser> getUsers() {
        return this.dao.findAll().stream().map(user -> Converter.convert(user, DtoUser.class)).toList();
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
        this.insert(user);
    }

    public EntityUser findByUsername(String username) {
        return this.dao.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return this.dao.existsByUsername(username);
    }

}
