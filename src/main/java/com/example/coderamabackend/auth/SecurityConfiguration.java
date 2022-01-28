package com.example.coderamabackend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService serviceUserAuth;

    // authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(serviceUserAuth).passwordEncoder(getPasswordEncoder());
    }

    // authorization (what you want)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.authorizeHttpRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/order/id/").hasAuthority("FULL_ADMIN");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new Sha256PasswordEncoder();
    }
}
