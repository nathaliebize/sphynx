package com.nathaliebize.sphynx.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.routing.SiteMap;

/**
 * Class that handles communication by email with one user.
 */
public class CommunicationByEmail {
    private User user;
    
    private String domain;
    
    public CommunicationByEmail(User user, String host) {
        this.user = user;
        this.domain = host;
    }
    
    public User getUser() {
        return user;
    }
    
    /**
     * Sends an email to confirm the user's email address while registering
     * @return link
     */
    public String sendConfirmationEmail() {
        // TODO: Send email
        return domain + SiteMap.USER_VERIFY.getPath() + "?email=" + this.user.getEmail() + "&key=" + this.user.getRegistrationKey();
    }
    
    /**
     * Sends an email to confirm the user's email address before reset password
     * @return link
     */
    public String sendResetPasswordEmail() {
        // TODO: Send email
        return domain + SiteMap.USER_RESET_PASSWORD.getPath() + "?email=" + this.user.getEmail() + "&key=" + this.user.getRegistrationKey();
    }

}
