package com.example.printifybackend.auth.filter;

import com.example.printifybackend.auth.CustomizedUserDetails;
import com.example.printifybackend.auth.JwtUtils;
import com.example.printifybackend.auth.ServiceUserAuth;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ServiceUserAuth serviceUserAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.jwtUtils.getJwtFromRequestHeader(request);
        if (StringUtils.isBlank(token) || !this.jwtUtils.validateJwtToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = this.jwtUtils.getUsernameFromJwtToken(token);
        CustomizedUserDetails userDetails = (CustomizedUserDetails) this.serviceUserAuth.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? Collections.emptySet() : userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
