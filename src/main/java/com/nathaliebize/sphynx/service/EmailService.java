package com.nathaliebize.sphynx.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.routing.SiteMap;

/**
 * Class that sent emails to users.
 */
@Service
public class EmailService {
    
    @Autowired
    public JavaMailSender javaMailSender;
        
    private User user;
    
    private String host;
        
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Sends an email to confirm registration
     */
    public void sendConfirmationRegistrationEmail() {
        String link = this.host + SiteMap.USER_VERIFY.getPath() + "?email=" + this.user.getEmail() + "&key=" + this.user.getRegistrationKey();
        String object = "Please confirm your registration.";
        String body = "<p>Confirm your registration to Shpynx.dev!</p>"
                + "<h3>To complete your accompte registration, please click on the following link.</h3>"
                + "<p>Then log with your email address and password.</p>"
                + "<a href=\"https://"
                + link
                + "\">"
                + link
                + "</a>"
                + "<p>Best regards,</p>"
                + "<p>Sphynx team</p>";
        
        MimeMessage message = javaMailSender.createMimeMessage();
          
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(this.user.getEmail());
            helper.setSubject(object);
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
    
    /**
     * Sends an email to confirm password request request
     */
    public void sendPasswordResetRequestConfirmationEmail() {
        String link = host + SiteMap.USER_RESET_PASSWORD.getPath() + "?email=" + this.user.getEmail() + "&key=" + this.user.getRegistrationKey();
        String object = "Complete your password reset request.";
        String body = "<p>Reset your password.</p>"
                + "<h3>To reset your password, please click on the following link.</h3>"
                + "<p>Then log with your email address and your new password.</p>"
                + "<a href=\"https://"
                + link
                + "\">"
                + link
                + "</a>"
                + "<p>Best regards,</p>"
                + "<p>Sphynx team</p>";
        
        MimeMessage message = javaMailSender.createMimeMessage();
          
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(this.user.getEmail());
            helper.setSubject(object);
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
}
