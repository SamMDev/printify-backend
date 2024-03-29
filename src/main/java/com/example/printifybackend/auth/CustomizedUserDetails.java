package com.example.printifybackend.auth;

import com.example.printifybackend.user.EntityUser;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author SamMDev
 */
public class CustomizedUserDetails implements UserDetails {

    private final EntityUser user;
    @Getter
    private final Set<Role> roles = new HashSet<>();
    @Getter
    private final Set<Privilege> privileges = new HashSet<>();

    public CustomizedUserDetails(EntityUser user) {
        this.user = user;
        this.roles.addAll(this.user.deserializeUserRoles());
        this.privileges.addAll(this.user.getAllPrivileges());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.isBlank(user.getRoles()) && StringUtils.isBlank(user.getPrivileges()))
            return Collections.emptySet();
        return this.privileges;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
