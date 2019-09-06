package com.nathaliebize.sphynx.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nathaliebize.sphynx.model.CommunicationByEmail;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.model.view.ResetPasswordUser;
import com.nathaliebize.sphynx.service.UserService;

/**
 * Controller that handles all user's views
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService = new UserService();
    
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
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
    public String register(Model model, @Valid @ModelAttribute RegisterUser registerUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_REGISTER.getPath();
        }
        User user = userService.registerNewUser(registerUser);
        if (user != null) {
            // TODO: send email with link
            CommunicationByEmail communicationByEmail = new CommunicationByEmail(user);
            String link = communicationByEmail.sendConfirmationEmail();
            model.addAttribute("link", link);
            return SiteMap.USER_VERIFY.getPath();
        } else {
            model.addAttribute("error", "email already used");
            return SiteMap.USER_REGISTER.getPath();
        }
    }
    

    /**
     * Handles verify get request. Tells user to check his emails or verifies the email when have parameter.
     * @param model
     * @param email
     * @param key
     * @param link
     * @return /sites view if email is verified. /error otherwise
     */
    @GetMapping("/verify")
    public String verifyEmail(Model model, @RequestParam String email, @RequestParam String key, @ModelAttribute("link") String link) {
        if (email == null || key == null ) {
            return SiteMap.REDIRECT_ERROR.getPath();
        }
        User user = userService.verifyEmailAndRegistrationKeyAndRegistrationStatus(email, key);
        if (user == null) {
            return SiteMap.REDIRECT_ERROR.getPath();
        }
        return SiteMap.REDIRECT_SITES.getPath();
    }
    
    /**
     * Handles forgot-password get request. Asks user to enter email address.
     * @param model
     * @return /user/resetPasswordEmail view
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordUser", new ForgotPasswordUser());
        return SiteMap.USER_FORGOT_PASSWORD.getPath();
    }
    
    /**
     * Handles forgot-password post request. Receives user's email address and send link to user.
     * @param model
     * @param forgotPasswordEmailUser
     * @param bindingResult
     * @return /user/verify if user is valid. /error otherwise
     */
    @PostMapping("forgot-password")
    public String sendPasswordResetLink(Model model, @Valid @ModelAttribute ForgotPasswordUser forgotPasswordUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_FORGOT_PASSWORD.getPath();
        }
        User user = userService.updateRegistrationKey(forgotPasswordUser);
        if (user == null) {
            return SiteMap.REDIRECT_ERROR.getPath();
        } else {
            // TODO: send email with link
            CommunicationByEmail communicationByEmail = new CommunicationByEmail(user);
            String link = communicationByEmail.sendResetPasswordEmail();
            model.addAttribute("link", link);
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
        User user = userService.verifyEmailandRegistrationKey(email, key);
        if (user != null) {
            model.addAttribute("resetPasswordUser", new ResetPasswordUser(key));
            return SiteMap.USER_RESET_PASSWORD.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR.getPath();
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
        userService.updatePassword(resetPasswordUser);
        return SiteMap.REDIRECT_SITES.getPath();
    }
    
    /**
     * Handles logout get request
     * @return redirection to home page
     */
    @GetMapping("/logout")
    public String logout(Model model) {
        return SiteMap.REDIRECT_HOME.getPath();
    }
}
