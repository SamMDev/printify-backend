package com.example.printifybackend.auth;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public DtoPrincipal mapUserDetailsToDto(CustomizedUserDetails details, String accessToken) {
        return DtoPrincipal.builder()
                .username(details.getUsername())
                .privileges(details.getPrivileges().stream().map(Privilege::name).collect(Collectors.toSet()))
                .accessToken(accessToken)
                .build();
    }
}
