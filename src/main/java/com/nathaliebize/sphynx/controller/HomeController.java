package com.nathaliebize.sphynx.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nathaliebize.sphynx.routing.SiteMap;

/**
 * Controller that handles home page.
 */
@Controller
public class HomeController {
    /**
     * Handles the home page GET request.
     * @return index template
     */
    @GetMapping(value={"/", "/index"})
    public String showHomePage(Model model) {
        return SiteMap.INDEX.getPath();
    }
    
    /**
     * Handles the terms page GET request.
     * @return terms template
     */
    @GetMapping("/terms")
    public String showTermsPage(Model model) {
        return SiteMap.TERMS.getPath();
    }
    
    /**
     * Handles the info page GET request.
     * @return info template
     */
    @GetMapping("/info")
    public String showInfoPage(Model model) {
        return SiteMap.INFO.getPath();
    }
    
    /**
     * Handles the script GET request.
     * @return js script.
     */
    @GetMapping("/generalScript")
    public String getGeneralScript() {
        return SiteMap.SCRIPT.getPath();
    }
}
