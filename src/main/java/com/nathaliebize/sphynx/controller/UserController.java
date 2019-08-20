package com.nathaliebize.sphynx.controller;

import com.nathaliebize.sphynx.model.LoginUser;
import com.nathaliebize.sphynx.model.ResetPasswordUser;
import com.nathaliebize.sphynx.model.RegisterUser;
import com.nathaliebize.sphynx.model.ResetPasswordEmailUser;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles all user's views
 * 
 * @author Nathalie Bize
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Handles login get request
     * @param model
     * @param request
     * @return the user/login template
     */
    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
        model.addAttribute("loginUser", new LoginUser());
        return "user/login";
    }
    
    /**
     * Handles login post request
     * @param user
     * @return the sites template
     */
    @PostMapping("/login")
    public String login(Model model, @Valid @ModelAttribute LoginUser loginUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        User user = userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            if (user.getRegistrationStatus().toString().equals("VERIFIED")) {
                request.getSession().setAttribute("userId", user.getId());
                return "redirect:/sites/";
            } else {
                return "/user/verify";
            }
        } else {
            model.addAttribute("error", "wrong email or password");
            return "/user/login";
        }
    }
    
    /**
     * Handles register get request
     * @param model
     * @return user/register template
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterUser());
        return "user/register";
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
            return "user/register";
        }
        if (userRepository.findByEmail(registerUser.getEmail()) == null) {
            User user = new User(registerUser.getEmail(), registerUser.getPassword());
            userRepository.save(user);
            // TODO: send email with link
            String link = user.sendConfirmationEmail();
            model.addAttribute("link", link);
            return "user/verify";
        } else {
            model.addAttribute("error", "email already used");
            return "user/register";
        }
    }
    
    /**
     * Handles verify get request. Tells user to check his emails or verifies the email when have parameter.
     * @param link
     * @param request
     * @return template
     */
    @GetMapping("/verify")
    public String verifyEmail(@ModelAttribute("link") String link, HttpServletRequest request) {
        String email = request.getParameter("email");
        String key = request.getParameter("key");
        if (email == null) {
            return "verify";
        }
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRegistrationKey().equals(key) && user.getRegistrationStatus().toString().equals("UNVERIFIED")) {
            userRepository.changeRegistrationStatus("VERIFIED", user.getEmail());
            request.getSession().setAttribute("userId", user.getId());
            return "redirect:/sites/";
        }
        return "redirect:/user/error";
    }
    
    /**
     * Handles forgot-password get request. Asks user to enter email address.
     * @param model
     * @return user/resetPasswordEmail template
     */
    @GetMapping("/forgot-password")
    public String showResetPasswordEmailPage(Model model) {
        model.addAttribute("resetPasswordEmailUser", new ResetPasswordEmailUser());
        return "user/forgot-password";
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
    public String sendPasswordResetLink(Model model, @Valid @ModelAttribute ResetPasswordEmailUser resetPasswordEmailUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/forgot-password";
        }
        User user = userRepository.findByEmail(resetPasswordEmailUser.getEmail());
        if (user == null) {
            return "/error";
        } else {
            user.generateRegistrationKey();
            userRepository.updateRegistrationKey(user.getRegistrationKey(), user.getEmail());
            // TODO: send email with link
            String link = user.sendResetPasswordEmail();
            model.addAttribute("link", link);
            return "user/verify";
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
            return "user/reset-password";
        } else {
            return "/error";
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
            return "user/reset-password";
        }
        userRepository.updatePassword(resetPasswordUser.getPassword(), resetPasswordUser.getRegistrationKey());
        request.getSession().setAttribute("userId", userRepository.findByRegistrationKey(resetPasswordUser.getRegistrationKey()).getId());
        return "redirect:/sites/";
    }
    
    /**
     * Handles logout get request
     * @return redirection to home page
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
