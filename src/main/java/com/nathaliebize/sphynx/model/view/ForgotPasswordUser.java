package com.nathaliebize.sphynx.model.view;

import com.nathaliebize.sphynx.security.constraint.EmailField;

/**
 * The ForgotPasswordUser class represents the user that notifies having forgotten his password.
 */
public class ForgotPasswordUser {
    @EmailField (notEmpty = true, max = 300, regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", messageRegex = "Invalid email address", messageMax = "Email must has less than 300 characters")
    private String email;
    
    private String registrationKey;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }
    
}
