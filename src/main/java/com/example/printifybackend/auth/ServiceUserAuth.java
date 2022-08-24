package com.example.printifybackend.auth;

import com.example.printifybackend.user.DaoUser;
import com.example.printifybackend.user.EntityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author SamMDev
 */
@RequiredArgsConstructor
@Service
public class ServiceUserAuth implements UserDetailsService {
    
    private final DaoUser daoUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(this.daoUser.findByUsername(username))
                .map(CustomizedUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }
}
