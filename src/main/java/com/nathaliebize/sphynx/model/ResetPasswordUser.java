package com.nathaliebize.sphynx.model;

import com.nathaliebize.sphynx.security.constraint.PasswordField;

@PasswordField(password = "password", passwordMatch = "confirmedPassword", min = 6, max = 124, notEmpty = true, messagePasswordMatch = "The password fields must match.", messageLength = "Password must be between 6 and 128 characters")
public class ResetPasswordUser {
    private String registrationKey;
    
    private String password;
    
    private String confirmedPassword;
    
    public ResetPasswordUser() {}
    
    public ResetPasswordUser(String registrationKey) {
        this.registrationKey = registrationKey;
    }
    
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

}
