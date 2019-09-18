package com.nathaliebize.sphynx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles home page.
 */
@Controller
public class HomeController {
    /**
     * Handles home page get request
     * @return index template
     */
    @GetMapping(value={"/", "/index"})
    public String showHomePage(Model model) {
        return SiteMap.INDEX.getPath();
    }
    
    /**
     * Handles terms page get request
     * @return template
     */
    @GetMapping("/terms")
    public String showTermsPage(Model model) {
        return SiteMap.TERMS.getPath();
    }
}
