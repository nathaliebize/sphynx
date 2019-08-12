package com.nathaliebize.sphynx.controller;

import com.nathaliebize.sphynx.model.LoginUser;
import com.nathaliebize.sphynx.model.RegisterUser;
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
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
        }
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            request.getSession().setAttribute("error", "email not registered");
            return "/user/login";
        }
        // TODO : verify password
        return "sites";
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
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (userRepository.findByEmail(registerUser.getEmail()) == null) {
            User user = new User(registerUser.getEmail(), registerUser.getPassword());
            userRepository.save(user);
            // TODO: send email with link
            @SuppressWarnings("unused")
            String link = user.sendConfirmationEmail();
            return "user/verify";
        } else {
            model.addAttribute("error", "email already used");
            return "user/register";
        }
    }
    
    @GetMapping("/verify")
    public String verifyEmail(HttpServletRequest request) {
        String email = null;
        String key = null;
        User user = null;
        String userKey = null;

        email = request.getParameter("email");
        key = request.getParameter("key");
        
        if (email == null) {
            return "verify";
        }
        user = userRepository.findByEmail(email);
        if (user != null) {
            userKey = user.getREGISTRATION_KEY();
            if (userKey.equals(key)) {
                // TODO change status to verified
                return "sites";
            }
        }
        return "user/error";
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
