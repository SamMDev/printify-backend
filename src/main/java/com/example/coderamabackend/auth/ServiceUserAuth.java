package com.example.coderamabackend.auth;

import com.example.coderamabackend.user.DaoUser;
import com.example.coderamabackend.user.EntityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author SamMDev
 */
@Service
public class ServiceUserAuth implements UserDetailsService {
    
    private final DaoUser daoUser;

    @Autowired
    public ServiceUserAuth(DaoUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityUser user = this.daoUser.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User with username " + username + "not found");

        return new CustomizedUserDetails(user);
    }
}
