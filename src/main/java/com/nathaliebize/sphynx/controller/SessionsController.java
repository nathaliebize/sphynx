package com.nathaliebize.sphynx.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles sessions pages.
 *
 */
public class SessionsController {
    /**
     * Handles sessions main page get request
     * @return the main sessions template
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/sessions")
    public String showSessionsPage() {
	return SiteMap.SESSIONS.getPath();
    }
}
