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
import com.nathaliebize.sphynx.model.view.SubmitedSite;
import com.nathaliebize.sphynx.routing.SiteMap;
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
     * Handles the GET request and displays the main page for logged users. 
     * It shows the user's updated list of the sites
     * or the form to registered a new site if none exists yet.
     * @param principal, the logged in user
     * @param model
     * @return the list of sites template or the create site form template
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSitesListPage(Principal principal, Model model) {
        ArrayList<Site> siteList = siteService.getSiteList(principal.getName());
        if (siteList.isEmpty()) {
            model.addAttribute("site", new SubmitedSite());
            return SiteMap.SITES_CREATE.getPath();
        }
        siteService.processData(principal.getName());
        model.addAttribute("sites", siteList);
        return SiteMap.SITES_INDEX.getPath();
    }
    
    /**
     * Handles the GET request and displays the list of sessions 
     * for one particular site of the logged in user.
     * @param principal, the logged in user
     * @param model
     * @param path variable siteId, the site id
     * @return the sessions list template or the error page.
     */
    @GetMapping("/{siteId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showSiteDetailsPage(Principal principal, Model model, @PathVariable final Long siteId) {
        ArrayList<Session> sessionList = siteService.getSessionList(principal.getName(), siteId);
        Site site = siteService.findBySiteId(siteId);
        if (sessionList != null && site != null) {
            model.addAttribute("site", site);
            model.addAttribute("sessionList", sessionList);
            return SiteMap.SESSIONS_INDEX.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }   
    }
    
    /**
     * Handles the GET request and displays the form to register a new site.
     * @param model
     * @return the create site form template.
     */
    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showCreatePage(Model model) {
        model.addAttribute("site", new SubmitedSite());
        return SiteMap.SITES_CREATE.getPath();
    }
    
    /**
     * Handles the POST request to register a new site in database for a logged in user.
     * Redirect to the confirmation page.
     * @param principal, the logged in user
     * @param model
     * @param created site, information about the new site to register
     * @return the create confirmation template.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createPage(Principal principal, Model model, @Valid @ModelAttribute SubmitedSite createdSite, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SiteMap.SITES_CREATE.getPath();
        }
        Long siteId = siteService.saveSite(createdSite, principal.getName());
        return "redirect:/sites/" + siteId + "/create";
    }
    
    /**
     * Handles GET requests and displays create confirmation page.
     */
    @GetMapping("/{siteId}/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCreateConfirmationPage(Principal principal, Model model, @PathVariable final Long siteId) {
        if (siteId != null) {
            String snippet = siteService.generateCodeSnippet(principal.getName(), siteId);
            if (snippet.equals("")) {
                return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
            }
            model.addAttribute("snippet", snippet);
            return SiteMap.SITES_CREATE_CONFIRMATION.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }
    }
    
    /**
     * Displays the confirmation page before deleting a registered site.
     * @return the delete site confirmation template.
     */
    @GetMapping("/{siteId}/delete-confirmation")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showDeleteConfirmationPage(Model model, @PathVariable final Long siteId) {
        Site site = siteService.findBySiteId(siteId);
        if (site != null) {
            model.addAttribute("site", site);
            return SiteMap.SITES_DELETE_CONFIRMATION.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }
    }
    
    /**
     * Deletes a registered site and displays site list page template.
     * @return the site list template.
     */
    @GetMapping("/{siteId}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteSite(Principal principal, Model model, @PathVariable final Long siteId) {
        if (siteService.deleteSite(principal.getName(), siteId)) {
            return SiteMap.REDIRECT_SITES.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }
    }
    
    /**
     * Displays the editing page.
     * @return the edit page template
     */
    @GetMapping("/{siteId}/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showEditPage(Principal principal, Model model, @PathVariable final Long siteId) {
        Site site = siteService.findBySiteId(siteId);
        if (site != null) {
            model.addAttribute("site", site);
            return SiteMap.SITES_EDIT.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }
    }
    
    /**
     * Edits a registered website info.
     * @return the site page template
     */
    @PostMapping("/{siteId}/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editSite(Principal principal, Model model, @PathVariable final Long siteId, @Valid @ModelAttribute Site site) {
        if (siteService.updateSite(siteId, site.getUrl(), site.getDescription())) {
            return SiteMap.REDIRECT_SITES.getPath();
        } else {
            return SiteMap.REDIRECT_ERROR_LOGOUT.getPath();
        }
    }
}
