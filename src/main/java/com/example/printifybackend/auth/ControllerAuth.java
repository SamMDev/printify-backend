package com.example.printifybackend.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.printifybackend.Converter;
import com.example.printifybackend.user.EntityUser;
import com.example.printifybackend.user.ServiceUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ControllerAuth {

    private final ServiceUserAuth serviceUserAuth;
    private final ServiceUser serviceUser;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No refresh token");
        }
        String refreshToken = authHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        EntityUser user = this.serviceUser.findByUsername(username);
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", this.serviceUser.getPrivilegesForUser(user).stream().map(Privileges::name).collect(Collectors.toList()))
                .sign(algorithm);
        Map<String, String> tokenMap = new HashMap<>(){
            {
                put("access_token", accessToken);
                put("refresh_token", refreshToken);
            }
        };
        response.setContentType(APPLICATION_JSON_VALUE);
        Converter.getObjectMapper().writeValue(response.getOutputStream(), tokenMap);
    }

}
