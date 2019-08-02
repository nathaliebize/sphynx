package com.nathaliebize.sphynx.controller;

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
        model.addAttribute("user", new User());
        return "user/login";
    }
    
    // TODO: user validation for login.
    /**
     * Handles login post request
     * @param user
     * @return the sites template
     */
    @PostMapping("/login")
    public String login(Model model, @ModelAttribute User user, HttpServletRequest request) {
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
        }
        User loginUser = userRepository.findByEmail(user.getEmail());
        if (loginUser == null) {
            request.getSession().setAttribute("error", "email not registered");
            return "/user/login";
        }
        return "sites";
    }
    
    /**
     * Handles register get request
     * @param model
     * @return user/register template
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }
    
    /**
     * Handles register post request
     * @param model
     * @param user
     * @param bindingResult
     * @return sites template if data are valid, user/register template otherwise
     */
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request) {
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        if (userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
            return "sites";
        } else {
            model.addAttribute("error", "email already used");
            return "user/register";
        }
    }
    
    /**
     * Handles logout get request
     * @return redirection to home page
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
