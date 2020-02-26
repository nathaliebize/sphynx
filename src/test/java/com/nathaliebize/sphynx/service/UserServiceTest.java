package com.nathaliebize.sphynx.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.User.RegistrationStatus;
import com.nathaliebize.sphynx.model.view.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.repository.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    
    @TestConfiguration
    static class UserServiceTestContextConfiguration {
  
        @Bean
        public UserService userService() {
            return new UserService();
        }
    }   
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Value("${sphynx.encoder.seed}")
    private String seed;
    
    @Before
    public void setup() {
        }
    
    
    @Test
    public void testRegisterNewUserAccount() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("email");
        registerUser.setPassword("password");
        registerUser.setAcceptedTerms(true);
        registerUser.setConfirmedPassword("password");        
        when(userRepository.findByEmail(registerUser.getEmail())).thenReturn(null);
    
        User renderedUser = userService.registerNewUser(registerUser);
        
        assertEquals("email", renderedUser.getEmail());
        assertTrue(new Pbkdf2PasswordEncoder(seed).matches("password", renderedUser.getPassword()));
        
    }
    
    @Test
    public void testRegisterNewUserAccount_userAlreadyExist() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("email");
        registerUser.setPassword("password");
        registerUser.setAcceptedTerms(true);
        registerUser.setConfirmedPassword("password"); 
        when(userRepository.findByEmail(registerUser.getEmail())).thenReturn(new User());
        
        User renderedUser = userService.registerNewUser(registerUser);
        
        assertNull(renderedUser);
    }

    @Test
    public void testverifyUser_email_key_status() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        user.setRegistrationStatus(RegistrationStatus.UNVERIFIED);
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("email", "key", "UNVERIFIED");
        
        assertNotNull(renderedUser);
        assertEquals("email", renderedUser.getEmail());
    }
    
    @Test
    public void testverifyUser_email_key_wrongStatus() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        user.setRegistrationStatus(RegistrationStatus.VERIFIED);
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("email", "key", "UNVERIFIED");
        
        assertNull(renderedUser);
    }
    
    @Test
    public void testverifyUser_email_wrongKey_Status() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        user.setRegistrationStatus(RegistrationStatus.UNVERIFIED);
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("email", "wrongKey", "UNVERIFIED");
        
        assertNull(renderedUser);
    }
    
    @Test
    public void testverifyUser_wrongEmail_key_Status() {
        User user = new User();
        user.setEmail("wrongEmail");
        user.setRegistrationKey("key");
        user.setRegistrationStatus(RegistrationStatus.UNVERIFIED);
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("wrongEmail", "key", "UNVERIFIED");
        
        assertNull(renderedUser);
    }

    @Test
    public void testVerify_email_key() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("email", "key");
        
        assertNotNull(renderedUser);
        assertEquals("email", renderedUser.getEmail());
    }
    
    @Test
    public void testVerify_email_wrongKey() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("email", "wrongKey");
        
        assertNull(renderedUser);
    }
    
    @Test
    public void testVerify_wrongEmail_key() {
        User user = new User();
        user.setEmail("email");
        user.setRegistrationKey("key");
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.verifyUser("wrongEmail", "key");
        
        assertNull(renderedUser);
    }

    @Test
    public void testUpdateRegistrationKey() {
        ForgotPasswordUser forgotPasswordUser = new ForgotPasswordUser();
        forgotPasswordUser.setEmail("email");
        User user = new User();
        user.setEmail("email");
        when(userRepository.findByEmail("email")).thenReturn(user);
        
        User renderedUser = userService.updateRegistrationKey(forgotPasswordUser);
        
        assertNotNull(renderedUser);
        assertEquals(forgotPasswordUser.getEmail(), user.getEmail());
    }

    @Test
    public void testUpdateRegistrationKey_userNotRegistered() {
        ForgotPasswordUser forgotPasswordUser = new ForgotPasswordUser();
        forgotPasswordUser.setEmail("email");
        when(userRepository.findByEmail("email")).thenReturn(null);
        
        User renderedUser = userService.updateRegistrationKey(forgotPasswordUser);
        
        assertNull(renderedUser);
    }
}
