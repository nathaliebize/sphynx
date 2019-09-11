package com.nathaliebize.sphynx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.service.SiteService;

/**
 * Controller that handles home page.
 *
 */
@Controller
public class HomeController {
    
    @Autowired
    SiteService siteService;
    
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
    
    /**
     * Receives data from sphynx-powered websites.
     * @return response body
     */
    @PostMapping("/save-event")
    public @ResponseBody String saveEvent(@RequestBody RecordedEvent jsonString) {
        siteService.saveEvent(jsonString);
        return "All good";
    }
    
    @PostMapping("/save-session")
    public @ResponseBody String saveSession(@RequestBody RecordedSession jsonString) {
        siteService.saveSession(jsonString);
        return "All good";
    }

}
