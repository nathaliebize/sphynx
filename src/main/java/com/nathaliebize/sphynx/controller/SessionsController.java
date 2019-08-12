package com.nathaliebize.sphynx.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles sessions pages.
 *
 * @author Nathalie Bize
 *
 */
public class SessionsController {
    /**
     * Sessions main page get request handler
     * @return the main sessions template
     */
    @GetMapping("/sessions")
    public String showSessionsPage() {
	return "sessions";
    }
}
