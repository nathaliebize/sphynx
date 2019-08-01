package com.nathaliebize.sphynx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }
    
    @GetMapping("/terms")
    public String showTermsPage() {
	return "terms";
    }
    
    @GetMapping("/sessions")
    public String showSessionsPage() {
	return "sessions";
    }

}