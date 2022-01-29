package com.example.printifybackend.auth;

import com.example.printifybackend.auth.filter.CustomAuthenticationFilter;
import com.example.printifybackend.auth.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // my implementation of UserDetailsService
    private final ServiceUserAuth userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    /**
     * Configuring http requests
     */
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(this.authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();
        // static controllers (because using tokens, not sessions)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // make access conditions
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/login", "/auth/token/refresh").permitAll();     // anyone can access this
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/item/**").permitAll();   // anyone can access items
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("MANAGE_ORDERS", "SHOW_ORDERS");  // only authorized users can access this
        // everyone doing request must be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // filters
        // authentication filter
        http.addFilter(customAuthenticationFilter);
        // authorization filter
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
