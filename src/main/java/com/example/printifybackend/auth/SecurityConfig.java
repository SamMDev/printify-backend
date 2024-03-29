package com.example.printifybackend.auth;

import com.example.printifybackend.auth.filter.AuthenticationUnauthorizedEntryPoint;
import com.example.printifybackend.auth.filter.AuthorizationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // my implementation of UserDetailsService
    private final ServiceUserAuth userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationTokenFilter authorizationTokenFilter;
    private final AuthenticationUnauthorizedEntryPoint unauthorizedEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();

        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // add unauthorized endpoint
        http = http.exceptionHandling().authenticationEntryPoint(this.unauthorizedEntryPoint).and();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http.authorizeRequests()

//                .antMatchers(HttpMethod.OPTIONS, "/auth/sign-in").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/auth/sign-up").permitAll()
//
//                // Our public endpoints
//                .antMatchers(HttpMethod.GET, "/item/all").hasAnyAuthority("ADD_PRODUCTS", "MANAGE_ORDERS", "SHOW_ORDERS")
//                .antMatchers(HttpMethod.GET, "/item/internet-visible").permitAll()
//                .antMatchers(HttpMethod.GET, "/item/internet-visible/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/order/get").hasAnyAuthority("MANAGE_ORDERS", "SHOW_ORDERS")
//
//                .antMatchers(HttpMethod.POST, "/auth/sign-up").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/sign-in").permitAll()
//
//                .antMatchers(HttpMethod.POST, "/order/save").permitAll()
//                .antMatchers(HttpMethod.POST, "/order/id/**").hasAnyAuthority("MANAGE_ORDERS", "SHOW_ORDERS")
//
//                .antMatchers(HttpMethod.POST, "/item/uuid").permitAll()
//                .antMatchers(HttpMethod.GET, "/item/uuid/**").permitAll()
//
//                .antMatchers(HttpMethod.GET, "/user/all").hasAuthority("MANAGE_USERS")
//
//                // Our private endpoints
//                .anyRequest().authenticated();
                    .anyRequest().permitAll();

        // add filter before every request (to check if is authorized)
        http.addFilterBefore(this.authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
