package com.nathaliebize.sphynx.model;

import com.nathaliebize.sphynx.controller.SiteMap;

/**
 * Class that handles communication by email with one user.
 *
 */
public class CommunicationByEmail {
    final private User user;
    
    public CommunicationByEmail(User user) {
        this.user = user;
    }
    
    /**
     * Sends an email to confirm the user's email address while registering
     * @return link
     */
    public String sendConfirmationEmail() {
        // TODO: Send email
        return SiteMap.URL_BASE.getPath() + SiteMap.USER_VERIFY.getPath() + "?email=" + this.user.getEmail() + "&key=" + this.user.getRegistrationKey();
    }
    
    /**
     * Sends an email to confirm the user's email address before reset password
     * @return link
     */
    public String sendResetPasswordEmail() {
        // TODO: Send email
        return SiteMap.URL_BASE.getPath() + SiteMap.USER_RESET_PASSWORD.getPath() + "?key=" + this.user.getRegistrationKey();
    }

}
