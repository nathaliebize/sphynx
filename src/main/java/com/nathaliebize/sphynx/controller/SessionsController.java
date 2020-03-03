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
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.SiteService;

/**
 * Controller that handles sessions pages.
 */
@Controller
@RequestMapping("/sessions")
public class SessionsController {
    @Autowired
    SiteService siteService;
    
    /**
     * Displays the timeline for one particular session.
     * @return the timeline template.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{sessionId}")
    public String showSessionTimelinePage(Principal principal, Model model, @PathVariable final String sessionId) {
        ArrayList<Event> eventList = siteService.getEventList(principal.getName(), sessionId);
        Session session = siteService.getSession(principal.getName(), sessionId);
        Site site = null;
        if (eventList !=  null && session != null) {
            site = siteService.getSite(principal.getName(), session.getSiteId());
        }
        if (site != null) {
            model.addAttribute("site", site);
            model.addAttribute("user_session", session);
            model.addAttribute("events", eventList);
        }
        return SiteMap.SESSIONS_TIMELINE.getPath();
    }
    
    /**
     * Displays the confirmation page to delete a session.
     * @return the delete session template.
     */
    @GetMapping("/{sessionId}/delete-confirmation")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showDeletePage(Principal principal, Model model, @PathVariable final String sessionId) {
        Session session = siteService.getSession(principal.getName(), sessionId);
        if (session != null) {
            model.addAttribute("sessionSphynx", session);
        }
        return SiteMap.SESSIONS_DELETE_REQUEST_CONFIRMATION.getPath();
    }
    
    /**
     * Deletes a session.
     * @return the delete site template.
     */
    @GetMapping("/{sessionId}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteSession(Principal principal, Model model, @PathVariable final String sessionId) {
        return (siteService.deleteSession(principal.getName(), sessionId)) ? SiteMap.REDIRECT_SITES.getPath() :
            SiteMap.REDIRECT_ERROR_LOGOUT.getPath(); 
    }
}
