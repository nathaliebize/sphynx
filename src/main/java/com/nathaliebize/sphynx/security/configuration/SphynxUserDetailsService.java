package com.nathaliebize.sphynx.security.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.AuthorizationGroup;
import com.nathaliebize.sphynx.model.AuthorizationGroupRepository;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.UserRepository;

@Service
public class SphynxUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;
    private final AuthorizationGroupRepository authorizationGroupRepository;
    
    public SphynxUserDetailsService(UserRepository userRepository, AuthorizationGroupRepository authorizationGroupRepository) {
        super();
        this.userRepository = userRepository;
        this.authorizationGroupRepository = authorizationGroupRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("cannot find user: " + email);
        }
        AuthorizationGroup authorizationGroup = this.authorizationGroupRepository.findByEmail(email);
        return new SphynxUserPrincipal(user, authorizationGroup);
    }

}
