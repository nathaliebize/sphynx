package com.nathaliebize.sphynx.model;

import javax.validation.constraints.Size;

import com.nathaliebize.sphynx.security.constraint.EmailField;

public class LoginUser {
    @EmailField (notEmpty = true, max = 300, regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", messageRegex = "Invalid email address", messageMax = "Email must has less than 300 characters")
    private String email;
    
    @Size(min=1, message="Must not be blank")
    private String password;

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
}
