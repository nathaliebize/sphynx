package com.nathaliebize.sphynx.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.model.view.ResetPasswordUser;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.EmailService;
import com.nathaliebize.sphynx.service.UserService;

/**
 * Controller that handles the user views
 */
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Handles login get request
     * @param model
     * @return the login view
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return SiteMap.USER_LOGIN.getPath();
    }
    
    /**
     * Handles register get request
     * @param model
     * @return register view
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterUser());
        return SiteMap.USER_REGISTER.getPath();
    }
    
    /**
     * Handles register post request
     * @param model
     * @param registerUser
     * @param bindingResult
     * @return /sites view if data are valid, /user/register view otherwise
     */
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute RegisterUser registerUser, BindingResult bindingResult, @RequestHeader String host) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_REGISTER.getPath();
        }
        User user = userService.registerNewUser(registerUser);
        if (user != null) {
            if (host != null) {
                emailService.setUser(user);
                emailService.setHost(host);
                emailService.sendConfirmationRegistrationEmail();
                return SiteMap.USER_VERIFY.getPath();
            } else {
                return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
            }
        } else {
            model.addAttribute("error", "email already used");
            return SiteMap.USER_REGISTER.getPath();
        }
    }
    
    /**
     * Handles forgot-password get request. Asks user to enter email address.
     * @param model
     * @return /user/resetPasswordEmail view
     */
    @GetMapping("/reset-password-request")
    public String showResetPasswordRequestPage(Model model) {
        model.addAttribute("forgotPasswordUser", new ForgotPasswordUser());
        return SiteMap.USER_RESET_PASSWORD_REQUEST.getPath();
    }
    
    /**
     * Handles forgot-password post request. Receives user's email address and send link to user.
     * @param model
     * @param forgotPasswordUser
     * @param bindingResult
     * @return /user/verify if user is valid. /error otherwise
     */
    @PostMapping("/reset-password-request")
    public String sendPasswordResetLink(Model model, @Valid @ModelAttribute ForgotPasswordUser forgotPasswordUser, BindingResult bindingResult, @RequestHeader String host) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_RESET_PASSWORD_REQUEST.getPath();
        }
        User user = userService.updateRegistrationKey(forgotPasswordUser);
        if (user == null) {
            return SiteMap.ERROR.getPath();
        } else {
            emailService.setUser(user);
            emailService.setHost(host);
            emailService.sendPasswordResetRequestConfirmationEmail();;
            return SiteMap.USER_VERIFY.getPath();
        }
    }
    
    /**
     * Handles reset-password get request. Asks user to enter new password and confirmation password.
     * @param model
     * @param email
     * @param key
     * @return /user/reset-password if parameters are correct. /error otherwise
     */
    @GetMapping("/reset-password")
    public String showResetPasswordPage(Model model, @RequestParam String email, @RequestParam String key) {
        User user = userService.verifyUser(email, key);
        if (user != null) {
            model.addAttribute("resetPasswordUser", new ResetPasswordUser(email, key));
            return SiteMap.USER_RESET_PASSWORD.getPath();
        } else {
            return SiteMap.ERROR.getPath();
        }
    }
    
    /**
     * Handles reset-password post request. Validates the new password and saves it.
     * @param model
     * @param resetPasswordUser
     * @param bindingResult
     * @param request
     * @return /sites
     */
    @PostMapping("/reset-password")
    public String resetPassword(Model model, @Valid @ModelAttribute ResetPasswordUser resetPasswordUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_RESET_PASSWORD.getPath();
        }
        if (userService.updatePassword(resetPasswordUser)) {
            return SiteMap.USER_RESET_PASSWORD_CONFIRMATION.getPath();
        }
        return SiteMap.ERROR.getPath();
    }
}
