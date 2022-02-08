package com.example.printifybackend.auth;

import com.example.printifybackend.user.DaoUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceAuth {

    public boolean isPasswordValid(String rawPassword) {
        return (StringUtils.isNotBlank(rawPassword) && rawPassword.length() >= 8);
    }

    public boolean isUsernameFormatValid(String username) {
        return (StringUtils.isNotBlank(username) && username.length() > 4 && username.length() <= 15);
    }

    public DtoPrincipal mapUserDetailsToDto(CustomizedUserDetails details) {
        return DtoPrincipal.builder()
                .username(details.getUsername())
                .roles(details.getRoles().stream().map(Role::name).collect(Collectors.toSet()))
                .privileges(details.getPrivileges().stream().map(Privilege::name).collect(Collectors.toSet()))
                .build();
    }
}
