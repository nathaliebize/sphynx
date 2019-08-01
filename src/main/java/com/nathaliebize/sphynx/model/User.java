package com.nathaliebize.sphynx.model;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.nathaliebize.sphynx.security.constraint.FieldMatch;


@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByEmail",
query = "select u from User u where u.email = ?1")
@FieldMatch(first = "password", second = "confirmedPassword", message = "The password fields must match")
public class User {
    @Id
    @GeneratedValue(generator = "users_generator")
    @SequenceGenerator(
            name = "users_generator",
            sequenceName = "users_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @NotBlank
    @Size(max = 300)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email address is invalid")
    private String email;
    
    @NotBlank
    @Size(min=6, max=128, message="Password name must be between 6 and 128 characters")
    private String password;
    
    @NotBlank
    @Size(min=6, max=128, message="Password name must be between 6 and 128 characters")
    private String confirmedPassword;
    
    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    @NotNull
    @AssertTrue(message="You must accept the terms and conditions.")
    private boolean acceptedTerms;


    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
	this.acceptedTerms = acceptedTerms;
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
    

}
