package com.nathaliebize.sphynx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sites")
public class SitesController {
    
    @GetMapping("/")
    public String showSitesPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return SiteMap.SITES.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR.getPath();
        }
    }
}
