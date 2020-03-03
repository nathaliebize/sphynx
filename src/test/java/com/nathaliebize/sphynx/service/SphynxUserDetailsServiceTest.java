package com.nathaliebize.sphynx.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.UserRepository;

public class SphynxUserDetailsServiceTest {

    UserRepository repository = mock(UserRepository.class);
    SphynxUserDetailsService sphynxUserService = new SphynxUserDetailsService(repository);
    
    @Test
    public void testLoadUserByUsername() {
        when(repository.findByEmail("email")).thenReturn(new User());
        
        UserDetails user = sphynxUserService.loadUserByUsername("email");
        
        assertNotNull(user);
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_userNotFound() {
        when(repository.findByEmail("email")).thenReturn(null);
        
        sphynxUserService.loadUserByUsername("email"); 
    }

}

