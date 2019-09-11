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
     * Handles sites main page get request
     * @return the main sites template
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
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSiteDetailsPage(Principal principal, Model model, @PathVariable final Long id) {
        ArrayList<Session> sessionList = siteService.getSessionList(principal.getName(), id);
        model.addAttribute("sessionList", sessionList);
        return SiteMap.SESSIONS.getPath();
    }
    
    
    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showCreatePage(Model model) {
        model.addAttribute("site", new CreatedSite());
        return SiteMap.SITES_CREATE.getPath();
    }
    
    @GetMapping("/create-confirmation")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showCreateConfirmationPage(Model model) {
        return SiteMap.SITES_CREATE_CONFIRMATION.getPath();
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createPage(Principal principal, Model model, @Valid @ModelAttribute CreatedSite createdSite, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.SITES_CREATE.getPath();
        }
        siteService.saveSite(createdSite, principal.getName());
        return SiteMap.SITES_CREATE_CONFIRMATION.getPath();
    }
    
    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showDeletePage() {
        return SiteMap.SITES_DELETE.getPath();
    }
    
}
