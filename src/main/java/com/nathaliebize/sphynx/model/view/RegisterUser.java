package com.nathaliebize.sphynx.model.view;

import javax.validation.constraints.AssertTrue;

import com.nathaliebize.sphynx.security.constraint.EmailField;
import com.nathaliebize.sphynx.security.constraint.PasswordField;

/**
 * The RegisterUser class represents the user that register to the sphynx application.
 *
 */
@PasswordField(password = "password", passwordMatch = "confirmedPassword", min = 6, max = 124, notEmpty = true, messagePasswordMatch = "The password fields must match.", messageLength = "Password must be between 6 and 128 characters")
public class RegisterUser {
    
    @EmailField (notEmpty = true, max = 300, regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", messageRegex = "Invalid email address", messageMax = "Email must has less than 300 characters")
    private String email;
    
    private String password;
    
    private String confirmedPassword;
    
    @AssertTrue(message="You must accept the terms and conditions.")
    private boolean acceptedTerms;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}
