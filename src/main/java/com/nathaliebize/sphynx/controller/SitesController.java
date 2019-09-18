package com.nathaliebize.sphynx.controller;

import java.security.Principal;
import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.view.CreatedSite;
import com.nathaliebize.sphynx.service.SiteService;

/**
 * Controller that handles sites pages.
 */
@Controller
@RequestMapping("/sites")
public class SitesController {
    @Autowired
    private SiteService siteService;
    
    /**
     * Displays the main page for logged users. It includes the user's list of the sites
     * or the form to registered a new site if any exist yet.
     * @param principal, the logged in user
     * @param model
     * @return the list of sites template or the create site form template
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSitesListPage(Principal principal, Model model) {
        ArrayList<Site> siteList = siteService.getSiteList(principal.getName());
        if (siteList.isEmpty()) {
            model.addAttribute("site", new CreatedSite());
            return SiteMap.SITES_CREATE.getPath();
        }
        model.addAttribute("sites", siteList);
        return SiteMap.SITES_INDEX.getPath();
    }
    
    /**
     * Displays the list of sessions for one particular user's site.
     * @param principal, the logged in user
     * @param model
     * @param path variable id, the site id
     * @return the sessions list template.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSiteDetailsPage(Principal principal, Model model, @PathVariable final Long id) {
        ArrayList<Session> sessionList = siteService.getSessionList(principal.getName(), id);
        Site site = siteService.findBySiteId(id);
        model.addAttribute("site", site);
        model.addAttribute("sessionList", sessionList);
        return SiteMap.SESSIONS.getPath();
    }
    
    /**
     * Displays the form to register a new site.
     * @param model
     * @return the create site form template.
     */
    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showCreatePage(Model model) {
        model.addAttribute("site", new CreatedSite());
        return SiteMap.SITES_CREATE.getPath();
    }
    
    /**
     * Handles the post request to register a new site.
     * @param principal, the logged in user
     * @param model
     * @param created site, information about the new site to register
     * @return the create confirmation template.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createPage(Principal principal, Model model, @Valid @ModelAttribute CreatedSite createdSite, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.SITES_CREATE.getPath();
        }
        siteService.saveSite(createdSite, principal.getName());
        return SiteMap.SITES_CREATE_CONFIRMATION.getPath();
    }
    
    /**
     * Displays the confirmation page with a code snippet to insert 
     * into the new sphynx-powered site.
     * @return the create confirmation template.
     */
    @GetMapping("/create-confirmation")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showCreateConfirmationPage() {
        return SiteMap.SITES_CREATE_CONFIRMATION.getPath();
    }
    
    /**
     * Displays the confirmation page to delete a registered site.
     * @return the delete site template.
     */
    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showDeletePage() {
        return SiteMap.SITES_DELETE.getPath();
    }
}
