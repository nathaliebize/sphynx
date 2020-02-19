package com.nathaliebize.sphynx.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.configuration.SphynxUserPrincipal;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.UserRepository;

/**
 * The SphynxUserDetailsService implements the Spring boot UserDetailsService.
 */
@Service
public class SphynxUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    
    public SphynxUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("cannot find user: " + email);
        }
        return new SphynxUserPrincipal(user);
    }

}
