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

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/login")
    public String showLoginPage(Model model, HttpServletRequest request) {
	model.addAttribute("user", new User());
	String error = (String) request.getSession().getAttribute("error");
	if (error != null) {
	    model.addAttribute("error", error);
	}
	return "user/login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
	User temp = userRepository.findByEmail(user.getEmail());
	if (temp == null) {
	    return "redirect:register";
	}
	return "sites";
    }
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
	model.addAttribute("user", new User());
	return "user/register";
    }
    
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute User user, BindingResult bindingResult) {
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
    
    @GetMapping("/logout")
    public String logout() {
	return "redirect:/";
    }
    
    

}
