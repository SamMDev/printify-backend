package com.example.printifybackend.auth;

import com.example.printifybackend.user.DaoUser;
import com.example.printifybackend.user.EntityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        EntityUser user = this.daoUser.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User with username " + username + " not found");

        return new CustomizedUserDetails(user);
    }
}
