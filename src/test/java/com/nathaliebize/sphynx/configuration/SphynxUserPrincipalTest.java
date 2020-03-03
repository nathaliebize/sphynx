package com.nathaliebize.sphynx.configuration;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.nathaliebize.sphynx.model.User;

public class SphynxUserPrincipalTest {

    @Test
    public void testGetAuthorities() {
        User user = new User();
        SphynxUserPrincipal userPrincipal = new SphynxUserPrincipal(user);
        
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        
        assertEquals("[ROLE_USER]", authorities.toString());
    }
    
    @Test
    public void testGetPassword() {
        User user = new User("email", "password");
        SphynxUserPrincipal userPrincipal = new SphynxUserPrincipal(user);
        
        String password = userPrincipal.getPassword();
        
        assertEquals("password", password);
    }
    
    @Test
    public void testGetUsername() {
        User user = new User("email", "password");
        SphynxUserPrincipal userPrincipal = new SphynxUserPrincipal(user);
        
        String email = userPrincipal.getUsername();
        
        assertEquals("email", email);
    }
    
    @Test
    public void testIsEnabled() {
        User user = new User();
        user.setRegistrationStatus(User.RegistrationStatus.VERIFIED);
        SphynxUserPrincipal userPrincipal = new SphynxUserPrincipal(user);
        
        boolean isEnabled = userPrincipal.isEnabled();
        
        assertTrue(isEnabled);
    }
    
    @Test
    public void testIsEnabled_userUnverified() {
        User user = new User();
        user.setRegistrationStatus(User.RegistrationStatus.UNVERIFIED);
        SphynxUserPrincipal userPrincipal = new SphynxUserPrincipal(user);
        
        boolean isEnabled = userPrincipal.isEnabled();
        
        assertFalse(isEnabled);
    }

}
