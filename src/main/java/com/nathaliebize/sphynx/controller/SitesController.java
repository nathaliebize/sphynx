package com.nathaliebize.sphynx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SitesController {
    
    @GetMapping("/sites")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSitesPage(HttpServletRequest request) {
        return "/sites";
    }
}
