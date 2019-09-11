package com.nathaliebize.sphynx.controller;

import java.security.Principal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.ViewEvent;
import com.nathaliebize.sphynx.service.SiteService;

/**
 * Controller that handles sessions pages.
 *
 */
@Controller
@RequestMapping("/sessions")
public class SessionsController {
    @Autowired
    SiteService siteService;
    
    /**
     * Handles sessions main page get request
     * @return the main sessions template
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public String showSessionTimelinePage(Principal principal, Model model, @PathVariable final Long id) {
        ArrayList<ViewEvent> viewEventList = siteService.getViewEventList(principal.getName(), id);
        model.addAttribute("events", viewEventList);
        return SiteMap.SESSIONS_TIMELINE.getPath();
    }
    
}
