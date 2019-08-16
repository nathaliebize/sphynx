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
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user == null) {
            model.addAttribute("error", "email not registered");
            return "/user/login";
        }
        // TODO : verify password
        user = null;
        user = userRepository.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            if (user.getStatus().equals("verified")) {
                request.getSession().setAttribute("id", user.getId());
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
    
    @GetMapping("/verify")
    public String verifyEmail(@ModelAttribute("link") String link, HttpServletRequest request) {
        String email = request.getParameter("email");
        String key = request.getParameter("key");
        if (email == null) {
            return "verify";
        }
        User user = userRepository.findByEmail(email);
        String userKey = null;
        if (user != null) {
            userKey = user.getREGISTRATION_KEY();
            if (userKey.equals(key)) {
                userRepository.changeStatus("verified", user.getEmail());
                request.getSession().setAttribute("loggedIn", true);
                return "error";
            }
        }
        return "redirect:/user/error";
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
