package com.nathaliebize.sphynx.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.repository.EventRepository;
import com.nathaliebize.sphynx.repository.SessionRepository;
import com.nathaliebize.sphynx.repository.SiteRepository;

@Service
public class DataService {
    @Autowired
    private SiteRepository siteRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    /**
     * Saves an event to database.
     * @param recordedEvent
     */
    public void saveEvent(RecordedEvent recordedEvent) {
        if (isValid(recordedEvent)) {
            Event event = new Event();
            event.setSessionId(recordedEvent.getSessionId());
            event.setSiteId(recordedEvent.getSiteId());
            event.setUserId(recordedEvent.getUserId());
            event.setDate(recordedEvent.getDate());
            event.setType(recordedEvent.getType());
            event.setPath(recordedEvent.getPath());            
            String target = recordedEvent.getTarget();
            if (target != null) {
                event.setTarget(target);
            }
            eventRepository.save(event);
        }
    }
    
    /**
     * Checks if a recorded event has a correct user id, site id and session id corresponding properties.
     * @param recordedEvent
     * @return true if correct
     */
    private boolean isValid(RecordedEvent recordedEvent) {
        if (recordedEvent == null) {
            return false;
        }
        // Check if the recorded session id match an existing session
        String recordedSessionId = recordedEvent.getSessionId();
        Long recordedUserId = recordedEvent.getUserId();
        Session session = sessionRepository.getSession(recordedUserId, recordedSessionId);
        if (session == null) {
            return false;
        }
        // Check if the site that sent the event is the matching recorded site.
        String recordedHost = null;
        String databaseSiteUrl = siteRepository.getSite(recordedUserId, session.getSiteId()).getUrl();
        if (databaseSiteUrl == null) {
            return false;
        }
        String databaseHost = null;
        try {
            recordedHost = new URL(recordedEvent.getPath()).getHost();
        } catch (Exception e) {
            return false;
        }
        try {
            databaseHost = new URL(databaseSiteUrl).getHost();
            return recordedHost.equals(databaseHost);
        } catch (Exception e) {
            databaseHost = databaseSiteUrl;
            return (recordedHost.equals(databaseHost) || recordedHost.contains(databaseHost) || databaseHost.contains(recordedHost));
        }
    }
    
    /**
     * Saves a new session to database given a recordedSession.
     * @param recordedSession
     */
    public void saveSession(RecordedSession recordedSession) {
        if (!isValid(recordedSession)) {
            return;
        }
        Session session = new Session();
        Long userId = recordedSession.getUserId();
        session.setUserId(userId);
        session.setSiteId(recordedSession.getSiteId());
        session.setId(recordedSession.getSessionId());
        session.setDate(recordedSession.getDate());
        session.setHost(recordedSession.getHost());
        sessionRepository.save(session);
    }
    
    /** 
     * Checks if the userId and siteId are matching
     * @param userId
     * @param siteId
     * @return
     */
    private boolean isValid(RecordedSession recordedSession) {
        if (recordedSession == null) {
            return false;
        }
        // Check if the site that sent the session is the matching recorded site.        
        Site databaseSite = siteRepository.getSite(recordedSession.getUserId(), recordedSession.getSiteId());
        if (databaseSite == null || !databaseSite.getUserId().equals(recordedSession.getUserId())) {
            return false;
        }
        String databaseSiteUrl = databaseSite.getUrl();
        if (databaseSiteUrl == null) {
            return false;
        }
        String recordedHost = recordedSession.getHost();
        String databaseHost = null;
        try {
            databaseHost = new URL(databaseSiteUrl).getHost();
        } catch (Exception e) {
            databaseHost = databaseSiteUrl;
        }
        return (recordedHost.equals(databaseHost) || recordedHost.contains(databaseHost) || databaseHost.contains(recordedHost));
    }
}
