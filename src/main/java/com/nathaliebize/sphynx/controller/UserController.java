package com.nathaliebize.sphynx.controller;

import com.nathaliebize.sphynx.model.AuthorizationGroup;
import com.nathaliebize.sphynx.model.CommunicationByEmail;
import com.nathaliebize.sphynx.model.ResetPasswordUser;
import com.nathaliebize.sphynx.model.RegisterUser;
import com.nathaliebize.sphynx.model.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.AuthorizationGroupRepository;
import com.nathaliebize.sphynx.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles all user's views
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
        
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationGroupRepository authorizationGroupRepository;
    
    /**
     * Handles login get request
     * @param model
     * @return the login template
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return SiteMap.USER_LOGIN.getPath();
    }
    
    /**
     * Handles register get request
     * @param model
     * @return register template
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
     * @return sites template if data are valid, user/register template otherwise
     */
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute RegisterUser registerUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_REGISTER.getPath();
        }
        if (userRepository.findByEmail(registerUser.getEmail()) == null) {
            User user = registerUser.registerUserToUser();
            userRepository.save(user);
            // TODO: send email with link
            CommunicationByEmail communicationByEmail = new CommunicationByEmail(user);
            model.addAttribute("link", communicationByEmail.sendConfirmationEmail());
            return SiteMap.USER_VERIFY.getPath();
        } else {
            model.addAttribute("error", "email already used");
            return SiteMap.USER_REGISTER.getPath();
        }
    }
    
    /**
     * Handles verify get request. Tells user to check his emails or verifies the email when have parameter.
     * @param link
     * @param request
     * @return template
     */
    @GetMapping("/verify")
    public String verifyEmail(Model model, @ModelAttribute("link") String link, HttpServletRequest request) {
        String email = request.getParameter("email");
        String key = request.getParameter("key");
        if (email == null) {
            return SiteMap.VERIFY.getPath();
        }
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRegistrationKey().equals(key) && user.getRegistrationStatus().toString().equals("UNVERIFIED")) {
            userRepository.changeRegistrationStatus("VERIFIED", user.getEmail());
            AuthorizationGroup authorizationGroup = new AuthorizationGroup(user.getEmail(), "USER");
            authorizationGroupRepository.save(authorizationGroup);
            request.getSession().setAttribute("userId", user.getId());
            return SiteMap.REDIRECT_USER_LOGIN.getPath();
        }
        return SiteMap.REDIRECT_ERROR.getPath();
    }
    
    /**
     * Handles forgot-password get request. Asks user to enter email address.
     * @param model
     * @return user/resetPasswordEmail template
     */
    @GetMapping("/forgot-password")
    public String showResetPasswordEmailPage(Model model) {
        model.addAttribute("resetPasswordEmailUser", new ForgotPasswordUser());
        return SiteMap.USER_FORGOT_PASSWORD.getPath();
    }
    
    /**
     * Handles forgot-password post request. Receives user's email address and send link to user.
     * @param model
     * @param resetPasswordEmailUser
     * @param bindingResult
     * @param request
     * @return template
     */
    @PostMapping("forgot-password")
    public String sendPasswordResetLink(Model model, @Valid @ModelAttribute ForgotPasswordUser resetPasswordEmailUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_FORGOT_PASSWORD.getPath();
        }
        User user = userRepository.findByEmail(resetPasswordEmailUser.getEmail());
        if (user == null) {
            return SiteMap.REDIRECT_ERROR.getPath();
        } else {
            user.generateRegistrationKey();
            userRepository.updateRegistrationKey(user.getRegistrationKey(), user.getEmail());
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
     * @param request
     * @return template
     */
    @GetMapping("/reset-password")
    public String showResetPasswordPage(Model model, HttpServletRequest request) {
        String key = (String) request.getParameter("key");
        ResetPasswordUser resetPasswordUser = null;
        if (key != null && (userRepository.findByRegistrationKey(key)) != null) {
            resetPasswordUser = new ResetPasswordUser(key);
            model.addAttribute("resetPasswordUser", resetPasswordUser);
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
     * @return template
     */
    @PostMapping("/reset-password")
    public String resetPassword(Model model, @Valid @ModelAttribute ResetPasswordUser resetPasswordUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return SiteMap.USER_RESET_PASSWORD.getPath();
        }
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        userRepository.updatePassword(encoder.encode(resetPasswordUser.getPassword()), resetPasswordUser.getRegistrationKey());
        request.getSession().setAttribute("userId", userRepository.findByRegistrationKey(resetPasswordUser.getRegistrationKey()).getId());
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
