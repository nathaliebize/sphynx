package com.nathaliebize.sphynx.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nathaliebize.sphynx.security.constraint.EmailField;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByEmail",
query = "select u from User u where u.email = ?1")
public class User {
    @Id
    @GeneratedValue(generator = "users_generator")
    @SequenceGenerator(
            name = "users_generator",
            sequenceName = "users_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @EmailField (notEmpty = true, max = 300, regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", messageRegex = "Invalid email address", messageMax = "Email must has less than 300 characters")
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private String REGISTRATION_KEY = UUID.randomUUID().toString();
    
    @NotNull
    private String Status = "unverified";
    
    public User() {}
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    
    public String getREGISTRATION_KEY() {
        return REGISTRATION_KEY;
    }

    /**
     * Sends an email to confirm the user's email address
     */
    public String sendConfirmationEmail() {
        return "http://sphynx.dev/user/verify?email=" + this.email + "&key=" + REGISTRATION_KEY;
    }

}
