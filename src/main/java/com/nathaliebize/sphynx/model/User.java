package com.nathaliebize.sphynx.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nathaliebize.sphynx.security.constraint.EmailField;

/**
 * User represents one user of Sphynx app. 
 * It is linked to the table users in database.
 */
@Entity
@Table(name = "users")
public class User {
    
    public enum RegistrationStatus {
        VERIFIED, UNVERIFIED;
    }
    
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
    @Column(name="registration_key")
    private String registrationKey = UUID.randomUUID().toString();
    
    @NotNull
    @Column(name="registration_status")
    private String registrationStatus = RegistrationStatus.UNVERIFIED.toString();
    
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
    
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus.toString();
    }
    
    public String getRegistrationKey() {
        return registrationKey;
    }
    
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    /**
     * Generate registration key
     */
    public String generateRegistrationKey() {
        return this.registrationKey = UUID.randomUUID().toString();
    }
}
