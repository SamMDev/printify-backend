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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ControllerAuth {

    private final ServiceUserAuth serviceUserAuth;
    private final ServiceUser serviceUser;
    private final ServiceAuth serviceAuth;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) throw new IllegalArgumentException("No refresh token");

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
                .withClaim("roles", this.serviceUser.getPrivilegesForUser(user).stream().map(Privilege::name).toList())
                .sign(algorithm);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        Converter.getObjectMapper().writeValue(response.getOutputStream(), tokenMap);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody SignUpRequest request) {
        final String username = request.getUsername();
        final String password = request.getPassword();

        if (this.serviceUser.existsByUsername(request.getUsername()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: username already taken"));

        if (!this.serviceAuth.isPasswordValid(password))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: invalid password, choose better one"));

        if (!this.serviceAuth.isUsernameFormatValid(username))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: invalid username, choose better one"));


        this.serviceUser.saveNewUser(username, password);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<DtoPrincipal> authenticateUser(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String accessToken = this.jwtUtils.generateAccessToken(authentication);

            CustomizedUserDetails userDetails = (CustomizedUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok()
                    .body(this.serviceAuth.mapUserDetailsToDto(userDetails, accessToken));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
