package com.nathaliebize.sphynx.security.configuration;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nathaliebize.sphynx.model.AuthorizationGroup;
import com.nathaliebize.sphynx.model.User;

public class SphynxUserPrincipal implements UserDetails{
    private static final long serialVersionUID = 1L;
    private User user;
    private AuthorizationGroup authorizationGroup;
    
    public SphynxUserPrincipal(User user, AuthorizationGroup authorizationGroup) {
        super();
        this.user = user;
        this.authorizationGroup = authorizationGroup;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorizationGroup == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(new SimpleGrantedAuthority(authorizationGroup.getAuthorizationGroup()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
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
        return user.getRegistrationStatus().equals("VERIFIED");
    }

}
