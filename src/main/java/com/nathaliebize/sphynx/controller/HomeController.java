package com.nathaliebize.sphynx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles home page
 *
 * @author Nathalie Bize
 *
 */
@Controller
public class HomeController {
    
    /**
     * Home page get request handler
     * @return the index template
     */
    @GetMapping("/")
    public String showHomePage() {
        return SiteMap.INDEX.getPath();
    }
    
    @GetMapping("/error")
    public String showErrorPage() {
        return SiteMap.ERROR.getPath();
    }
}