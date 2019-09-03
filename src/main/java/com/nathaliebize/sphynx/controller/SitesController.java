package com.nathaliebize.sphynx.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles sites pages.
 *
 */
@Controller
public class SitesController {
    
    /**
     * Handles sites main page get request
     * @return the main sites template
     */
    @GetMapping("/sites")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSitesPage() {
        return "/sites";
    }
}
