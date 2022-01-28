package com.example.coderamabackend.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final String SECRET_KEY = "SECRET";

    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Create JWT based on given user details
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return this.createToken(claims, userDetails.getUsername());
    }

    /**
     *
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = this.extractUsername(token);
        return (StringUtils.equals(username, userDetails.getUsername()) && !isTokenExpired(token));
    }
}
