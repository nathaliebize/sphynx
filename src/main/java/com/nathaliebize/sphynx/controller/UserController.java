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
        if (userRepository.findByEmail(loginUser.getEmail()) == null) {
            model.addAttribute("error", "email not registered");
            return "/user/login";
        }
        User user = userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            if (user.getStatus().equals("verified")) {
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
     * Handles verify get request
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
        if (user != null) {
            if (user.getRegistrationKey().equals(key) && user.getStatus().equals("unverified")) {
                userRepository.changeStatus("verified", user.getEmail());
                request.getSession().setAttribute("userId", user.getId());
                return "redirect:/sites/";
            }
        }
        return "redirect:/user/error";
    }
    
    /**
     * Handles resetPasswordEmail get request
     * @param model
     * @return user/resetPasswordEmail template
     */
    @GetMapping("/resetPasswordEmail")
    public String showResetPasswordEmailPage(Model model) {
        model.addAttribute("resetPasswordEmailUser", new ResetPasswordEmailUser());
        return "user/resetPasswordEmail";
    }
    
    /**
     * Handles resetPasswordEmail post request
     * @param model
     * @param resetPasswordEmailUser
     * @param bindingResult
     * @param request
     * @return template
     */
    @PostMapping("resetPasswordEmail")
    public String sendPasswordResetLink(Model model, @Valid @ModelAttribute ResetPasswordEmailUser resetPasswordEmailUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/resetPasswordEmail";
        }
        User user = userRepository.findByEmail(resetPasswordEmailUser.getEmail());
        if (user == null) {
            return "/error";
        } else {
            user.updateRegistrationKey();
            userRepository.updateRegistrationKey(user.getRegistrationKey(), user.getEmail());
            // TODO: send email with link
            String link = user.sendResetPasswordEmail();
            model.addAttribute("link", link);
            return "user/verify";
        }
    }
    /**
     * Handles resetPassword get request
     * @param model
     * @param request
     * @return template
     */
    @GetMapping("/resetPassword")
    public String showResetPasswordPage(Model model, HttpServletRequest request) {
        String key = (String) request.getParameter("key");
        ResetPasswordUser resetPasswordUser = null;
        if (key != null && (userRepository.findByRegistrationKey(key)) != null) {
            resetPasswordUser = new ResetPasswordUser(key);
            model.addAttribute("resetPasswordUser", resetPasswordUser);
            return "user/resetPassword";
        } else {
            return "/error";
        }
    }
    
    /**
     * Handles resetPassword post request
     * @param model
     * @param resetPasswordUser
     * @param bindingResult
     * @param request
     * @return template
     */
    @PostMapping("/resetPassword")
    public String resetPassword(Model model, @Valid @ModelAttribute ResetPasswordUser resetPasswordUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/resetPassword";
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
