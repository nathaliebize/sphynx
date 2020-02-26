package com.nathaliebize.sphynx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.model.view.ResetPasswordUser;
import com.nathaliebize.sphynx.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Value("${sphynx.encoder.seed}")
    private String seed;
        
    public UserService() {
    }
    
    /**
     * Register a new user to database if not existing.
     * @param registerUser
     * @return User if one is created, null if user were already in database.
     */
    public User registerNewUser(RegisterUser registerUser) {
        User user = findUser(registerUser);
        if (user == null) {
            user = createUser(registerUser);
            this.userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    /**
     * Find an User and verify if it has the correct Registration key 
     * and the registration status unverified.
     * @param email
     * @param key
     * @param status
     * @return user if verified, null if not.
     */
    public User verifyUser(String email, String key, String status) {
        User user = findUser(email);
        if (user != null && user.getRegistrationKey().equals(key) && user.getRegistrationStatus().toString().equals(status)) {
            this.userRepository.changeRegistrationStatus("VERIFIED", user.getEmail());
            return user;
        } else {
            return null;
        }        
    }
    
    /**
     * Find an User and verify if it has the correct Registration key
     * @param email
     * @param key
     * @return user if verified, null if not.
     */
    public User verifyUser(String email, String key) {
        User user = findUser(email);
        if (user != null && user.getRegistrationKey().equals(key)) {
            return user;
        } else {
            return null;
        } 
    }
    
    /**
     * Update the registration key of an user given an ForgotPasswordUser
     * @param forgotPasswordUser
     * @return user if it exists and key has been updated or null
     */
    public User updateRegistrationKey(ForgotPasswordUser forgotPasswordUser) {
        User user = findUser(forgotPasswordUser.getEmail());
        if (user != null) {
            user.generateRegistrationKey();
            this.userRepository.updateRegistrationKey(user.getRegistrationKey(), user.getEmail());
            return user;
        } else {
            return null;
        }
    }

    /**
     * Update the password of user after verify the registration key.
     * @param resetPasswordUser
     */
    public void updatePassword(ResetPasswordUser resetPasswordUser) {
        if (resetPasswordUser != null && resetPasswordUser.getPassword() != null && resetPasswordUser.getRegistrationKey() != null) {
            this.userRepository.updatePassword(new Pbkdf2PasswordEncoder(seed).encode(resetPasswordUser.getPassword()), resetPasswordUser.getRegistrationKey());
        }
    }
    
    /**
     * Create a new User given a RegisterUser.
     * @param registerUser
     * @return User
     */
    private User createUser(RegisterUser registerUser) {
        return new User(registerUser.getEmail(), new Pbkdf2PasswordEncoder(seed).encode(registerUser.getPassword()));
    }

    /**
     * Find a User in database given a registerUser.
     * @param registerUser
     * @return User if it exist or null.
     */
    private User findUser(RegisterUser registerUser) {
        return this.userRepository.findByEmail(registerUser.getEmail());
    }
    
    /**
     * Find a User in database given its email.
     * @param email
     * @return User if it exists or null.
     */
    private User findUser(String email) {
        return this.userRepository.findByEmail(email);
    }
}
