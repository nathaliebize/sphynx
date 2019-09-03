package com.nathaliebize.sphynx.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    private final Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder("JDfidlqmepzRE34fdjsklWWrIfj");
    
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Pbkdf2PasswordEncoder getEncoder() {
        return encoder;
    }

    public User registerNewUser(RegisterUser registerUser) {
        User user = verifyEmail(registerUser.getEmail());
        if (user != null) {
            return null;
        } else {
            user = new User(registerUser.getEmail(), encoder.encode(registerUser.getPassword()));
            this.userRepository.save(user);
            return user;
        }
    }
    
    public User verifyEmailAndRegistrationKeyAndRegistrationStatus(String email, String key) {
        User user = verifyEmailandRegistrationKey(email, key);
        if (user != null && user.getRegistrationStatus().toString().equals("UNVERIFIED")) {
            this.userRepository.changeRegistrationStatus("VERIFIED", user.getEmail());
            return user;
        }
        return null;
    }

    public User verifyEmailandRegistrationKey(String email, String key) {
        if (email != null) {
           User user = verifyEmail(email);
           if (user != null && user.getRegistrationKey().equals(key)) {
               return user;
           }
        }
        return null;
    }
    
    private User verifyEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    
    public User updateRegistrationKey(ForgotPasswordUser forgotPasswordUser) {
        User user = verifyEmail(forgotPasswordUser.getEmail());
        if (user == null) {
            return null;
        } else {
            user.generateRegistrationKey();
            this.userRepository.updateRegistrationKey(user.getRegistrationKey(), user.getEmail());
            return user;
        }
    }

    public void updatePassword(ResetPasswordUser resetPasswordUser) {
        this.userRepository.updatePassword(encoder.encode(resetPasswordUser.getPassword()), resetPasswordUser.getRegistrationKey());
    }

}
