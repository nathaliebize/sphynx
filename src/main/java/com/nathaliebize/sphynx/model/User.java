package com.nathaliebize.sphynx.model;

import com.nathaliebize.sphynx.model.RegistrationStatus;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nathaliebize.sphynx.security.constraint.EmailField;

@Entity
@Table(name = "users")
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
    
    public @NotNull String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(@NotNull String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
    
    public String getRegistrationKey() {
        return registrationKey;
    }
    
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    /**
     * Sends an email to confirm the user's email address while registering
     * @return link
     */
    public String sendConfirmationEmail() {
        // TODO: Send email
        return "http://sphynx.dev/user/verify?email=" + this.email + "&key=" + registrationKey;
    }
    
    /**
     * Update registration key
     */
    public void generateRegistrationKey() {
        this.registrationKey = UUID.randomUUID().toString();
    }
    
    /**
     * Sends an email to confirm the user's email address before reset password
     * @return link
     */
    public String sendResetPasswordEmail() {
        return "http://sphynx.dev/user/resetPassword?key=" + registrationKey;
    }

}
