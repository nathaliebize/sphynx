package com.nathaliebize.sphynx.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles terms page.
 *
 *
 */
public class TermsController {
    /**
     * Handles terms page get request
     * @return the terms template
     */
    @GetMapping("/terms")
    public String showTermsPage() {
	return SiteMap.TERMS.getPath();
    }
}
