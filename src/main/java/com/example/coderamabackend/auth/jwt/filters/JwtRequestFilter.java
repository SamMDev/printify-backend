package com.example.coderamabackend.auth.jwt.filters;

import com.example.coderamabackend.auth.ServiceUserAuth;
import com.example.coderamabackend.auth.jwt.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ServiceUserAuth serviceUserAuth;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    /**
     * Look at the request header (should contain jwt key) and determine
     * if the key is valid
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        final String jwt = this.extractTokenFromHeader(authHeader);
        final String username = this.jwtUtil.extractUsername(jwt);

        if (!StringUtils.isBlank(username) && SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = this.serviceUserAuth.loadUserByUsername(username);
            if (this.jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);

    }

    /**
     * Extracts JWT token from given string
     *
     * @param header    header as string containing JWT
     * @return          extracted JWT
     */
    private String extractTokenFromHeader(String header) {
        if (StringUtils.isBlank(header)) return null;
        if (!header.startsWith("Bearer ")) return null;

        return header.substring(7);
    }
}
