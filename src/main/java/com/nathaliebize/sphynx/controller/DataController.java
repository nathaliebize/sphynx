package com.nathaliebize.sphynx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.service.SiteService;

/**
 * Rest controller that handles the events and sessions data sent to the server.
 */
@RestController
public class DataController {
    
    @Autowired
    SiteService siteService;
    
    /**
     * Receives data from sphynx-powered websites.
     * Save each event into the events table.
     */
    @PostMapping("/save-event")
    public void saveEvent(@RequestBody RecordedEvent jsonString) {
        siteService.saveEvent(jsonString);
    }
    
    /**
     * Receives data from sphynx-powered websites.
     * Save each new session into the sessions table.
     */
    @PostMapping("/save-session")
    public void saveSession(@RequestBody RecordedSession jsonString) {
        siteService.saveSession(jsonString);
    }
    
    @PostMapping("/save-quit")
    public void saveExitEvent(@RequestBody RecordedEvent jsonString) {
        siteService.processData(jsonString.getUserId());
    }

}
