package com.nathaliebize.sphynx.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Class that keep record of the authorization group of one users.
 *
 */

@Entity
@Table(name="AUTHORIZATION_USER_GROUP")
public class AuthorizationGroup {
    @Id
    @Column(name="AUTHORIZATION_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name="EMAIL")
    private String email;
    
    @NotNull
    @Column(name="AUTHORIZATION_GROUP")
    private String authorizationGroup;
    
    public AuthorizationGroup() {}
    
    public AuthorizationGroup(String email, String authorizationGroup) {
        this.email = email;
        this.authorizationGroup = authorizationGroup;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthorizationGroup() {
        return authorizationGroup;
    }

    public void setAuthorizationGroup(String authorizationGroup) {
        this.authorizationGroup = authorizationGroup;
    }
}
