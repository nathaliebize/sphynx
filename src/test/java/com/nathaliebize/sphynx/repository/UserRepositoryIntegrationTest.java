package com.nathaliebize.sphynx.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
   
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailTest() {
        String emailAddress = "email@email.com";
        User user = new User(emailAddress, "password");
        userRepository.save(user);
        
        User renderedUser = userRepository.findByEmail(emailAddress);
        
        assertEquals(renderedUser.getEmail(), user.getEmail());
    }
    
    @Test
    public void findByEmailTest_userNotFound() {
        User renderedUser = userRepository.findByEmail("wrongEmail@email.com");
        
        assertNull(renderedUser);
    }
    
    @Test
    public void testUpdateRegistrationStatus() {
        User toUpdateUser = new User("email@gmail.com", "password");
        userRepository.save(toUpdateUser);
        
        userRepository.updateRegistrationStatus("VERIFIED", "email@gmail.com");
        
        User renderedUser = userRepository.findByEmail("email@gmail.com");
        assertEquals("email@gmail.com", renderedUser.getEmail());
        assertEquals("VERIFIED", renderedUser.getRegistrationStatus());
    }
    
    @Test
    public void testUpdatePassword() {
        User toUpdateUser = new User("email@gmail.com", "password");
        userRepository.save(toUpdateUser);
        
        userRepository.updatePassword("updatePassword", toUpdateUser.getRegistrationKey());
        
        User renderedUser = userRepository.findByEmail("email@gmail.com");
        assertEquals("email@gmail.com", renderedUser.getEmail());
        assertEquals("updatePassword", renderedUser.getPassword());
    }
    
    @Test
    public void testUpdateRegistrationKey() {
        User toUpdateUser = new User("email@gmail.com", "password");
        userRepository.save(toUpdateUser);
        
        userRepository.updateRegistrationKey("new registration key", toUpdateUser.getEmail());
        
        User renderedUser = userRepository.findByEmail("email@gmail.com");
        assertEquals("email@gmail.com", renderedUser.getEmail());
        assertEquals("new registration key", renderedUser.getRegistrationKey());
    }
}
