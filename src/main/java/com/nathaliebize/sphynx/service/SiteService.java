package com.nathaliebize.sphynx.service;

import java.net.URL;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.SubmitedSite;
import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.repository.EventRepository;
import com.nathaliebize.sphynx.repository.SessionRepository;
import com.nathaliebize.sphynx.repository.SiteRepository;
import com.nathaliebize.sphynx.repository.UserRepository;

@Service
public class SiteService {
    
    @Autowired
    private SiteRepository siteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private EventRepository eventRepository;    
    
    /**
     * Saves a site into the database. It takes a SubmitedSite object and convert it to
     * a Site object.
     * @param createdSite
     * @param principalName
     * @return the site id if user could be retrive, null otherwise.
     */
    public Long saveSite(SubmitedSite submitedSite, String principalName) {
        Long userId = getUserId(principalName);
        if (userId != null) {
            Site site = new Site();
            site.setUserId(userId);
            site.setUrl(submitedSite.getUrl());
            site.setDescription(submitedSite.getDescription());
            siteRepository.save(site);
            return site.getId();
        } else {
            return null;
        }
    }
    
    /**
     * Saves a new session to database given a recordedSession.
     * @param recordedSession
     */
    public void saveSession(RecordedSession recordedSession) {
        if (validate(recordedSession)) {
            Session session = new Session();
            Long userId = recordedSession.getUserId();
            session.setUserId(userId);
            session.setSiteId(recordedSession.getSiteId());
            session.setSessionId(recordedSession.getSessionId());
            session.setStartTime(recordedSession.getStartTime());
            sessionRepository.save(session);
            processData(userId);
        } 
    }
    
    /**
     * Check if a recorded session has a correct user id and site id corresponding properties.
     * @param recordedSession
     * @return true if correct
     */
    private boolean validate(RecordedSession recordedSession) {
        if (recordedSession != null) {
            Long recordedUserId = recordedSession.getUserId();
            Long recordedSiteId = recordedSession.getSiteId();
            Site site = findBySiteId(recordedSiteId);        
            return (site != null && recordedUserId.equals(site.getUserId()));
        } else {
            return false;
        }
    }
    
    /**
     * Saves an event to database.
     * @param recordedEvent
     */
    public void saveEvent(RecordedEvent recordedEvent) {
        if (validate(recordedEvent)) {
            Event event = new Event();
            event.setSessionId(recordedEvent.getSessionId());
            event.setSiteId(recordedEvent.getSiteId());
            event.setUserId(recordedEvent.getUserId());
            event.setTimestamp(recordedEvent.getTimestamp());
            event.setType(recordedEvent.getType());
            
            String path = recordedEvent.getPath();
            if (path != null) {
                event.setPath(path);
            }
            String target = recordedEvent.getTarget();
            if (target != null) {
                event.setTarget(target);
            }
            eventRepository.save(event);
        }
    }
    
    /**
     * Check if a recorded event has a correct user id, site id and session id corresponding properties.
     * @param recordedEvent
     * @return true if correct
     */
    private boolean validate(RecordedEvent recordedEvent) {
        Long recordedUserId = null;
        Long recordedSiteId = null;
        Session session = null;
        String recordedHost = null;
        String databaseHost = null;
        try {
            recordedUserId = recordedEvent.getUserId();
            recordedSiteId = recordedEvent.getSiteId();
            String recordedSessionId = recordedEvent.getSessionId();
            URL recordedUrl = new URL(recordedEvent.getPath());
            recordedHost = recordedUrl.getHost();        
            session = findBySessionId(recordedSessionId);
            URL databaseUrl = null;
            databaseUrl = new URL(siteRepository.findBySiteId(session.getSiteId()).getUrl());
            databaseHost = databaseUrl.getHost();
        } catch (Exception e) {
            return false;
        }
        return (recordedHost != null && databaseHost != null && recordedHost.equals(databaseHost) && recordedSiteId.equals(session.getSiteId()) && recordedUserId.equals(session.getUserId()));
    }
    
    /**
     * Returns an arraylist of sites for a given principal user's name.
     * @param principalName
     * @return list of sites
     */
    public ArrayList<Site> getSiteList(String principalName) {
        Long userId = getUserId(principalName);
        ArrayList<Site> siteList = null;
        if (userId != null) {
            siteList = siteRepository.getSiteList(userId);
        }
        return siteList;
    }
    
    /**
     * Returns an arraylist of sites for one given user id.
     * @param userId
     * @return list of sites
     */
    public ArrayList<Site> getSiteList(Long userId) {
        return siteRepository.getSiteList(userId);
    }

    /**
     * Returns an arraylist of sessions for one given userId and site id pair.
     * @param userId
     * @param siteId
     * @return list of sessions
     */
    public ArrayList<Session> getSessionList(Long userId, Long siteId) {
        return sessionRepository.getSessionList(userId, siteId);
    }
    
    /**
     * Returns an arraylist of sessions for a given principal user's name and site id pair.
     * @param principalName
     * @param siteId
     * @return list of sessions
     */
    public ArrayList<Session> getSessionList(String principalName, Long siteId) {
        Long userId = getUserId(principalName);
        ArrayList<Session> sessionList = null;
        if (userId != null) {
            sessionList = sessionRepository.getSessionList(userId, siteId);
        }
        return sessionList;
    }
    
    /**
     * Returns an arraylist of events for a given session, using the session id and user id.
     * @param userId
     * @param sessionId
     * @return list of events
     */
    public ArrayList<Event> getEventList(Long userId, String sessionId) {
        return eventRepository.getEventList(userId, sessionId);
    }

    /**
     * Returns an arraylist of events for a given session, using the principal's name and the session id.
     * @param principalName
     * @param sessionId
     * @return list of events
     */
    public ArrayList<Event> getEventList(String principalName, String sessionId) {
        Long userId = getUserId(principalName);
        ArrayList<Event> eventList = new ArrayList<Event>();
        if (userId != null) {
            eventList = eventRepository.getEventList(userId, sessionId);
        }
        return eventList;
    }
    
    /**
     * Returns the user id for a given principal user's name.
     * @param principalName
     * @return user id
     */
    private Long getUserId(String principalName) {
        User user = userRepository.findByEmail(principalName);
        if (user != null) {
            return user.getId();
        } else {
            return -1L;
        }
    }

    /**
     * Return a site from database for a given site id.
     * @param site id
     * @return site
     */
    public Site findBySiteId(Long siteId) {
        return siteRepository.findBySiteId(siteId);
    }
    
    /**
     * Return a session from database for a give session id.
     * @param sessionId
     * @return session
     */
    public Session findBySessionId(String sessionId) {
        return sessionRepository.findBySessionId(sessionId);
    }

    /**
     * Update size (number of session) of a given site.
     * @param site
     */
    private void updateSiteSize(Site site) {
        if (site != null) {
            ArrayList<Session> sessionList = sessionRepository.getSessionList(site.getUserId(), site.getId());
            int siteSize = -1;
            if (sessionList != null) {
                siteSize = sessionList.size();
            }
            if (siteSize != -1 && siteSize != site.getSize()) {
                siteRepository.updateSiteSize(site.getId(), siteSize);
            }
        }
    }
    
    /**
     * Process the data in database to update number of session for a given user id.
     * @param userId
     */
    public void processData(Long userId) {
        ArrayList<Site> siteList = getSiteList(userId);
        if (siteList != null) {
            for (Site site : siteList) {
                updateSiteSize(site);
            }
        }
    }
    
    /**
     * Process the data in database to update number of session for a given name.
     * @param principalName
     */
    public void processData(String principalName) {
        Long userId = getUserId(principalName);
        if (userId != null) {
            processData(userId);
        }
    }

    /**
     * Deletes a site and its related sessions for a given siteId and a given user from the databse.
     * @param principalName
     * @param siteId
     * @return true if site was deleted
     */
    public boolean deleteSite(String principalName, Long siteId) {
        Long userId = getUserId(principalName);
        Site site = siteRepository.findBySiteId(siteId);
        if (site != null && site.getUserId().equals(userId)) {
            siteRepository.delete(site);
            ArrayList<Session> sessionList = getSessionList(principalName, siteId);
            if (sessionList != null) {
                for (Session session : sessionList) {
                    deleteSession(principalName, session.getSessionId());
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Deletes all sessions for a given site id and a given user from the databse.
     * @param principalName
     * @param sessionId
     */
    public boolean deleteSession(String principalName, String sessionId) {
        Long userId = getUserId(principalName);
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session != null && session.getUserId().equals(userId)) {
            sessionRepository.deleteBySessionId(sessionId);
            processData(userId);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Validates if a site belong to a user, given the site id and principal name.
     * @param principalName
     * @param siteId
     * @return true if correct
     */
    public boolean validate(String principalName, long siteId) {
        Long siteUserId = findBySiteId(siteId).getUserId();
        Long principalUserId = userRepository.findByEmail(principalName).getId();
        return siteUserId.equals(principalUserId);
    }
    
    /**
     * Generates the code snippet with the correct user id and site id for a given user and a given site.
     * @param principalName
     * @param siteId
     * @return the code snippet.
     */
    public String generateCodeSnippet(String principalName, long siteId) {
        if (validate(principalName, siteId)) {
            Long userId = getUserId(principalName);
            StringBuilder snippet = new StringBuilder();
            snippet.append("<script type='text/javascript'>\n"
                    + "(function() {\n"
                    + "let script = document.createElement('script');\n"
                    + "script.type = 'text/javascript';\n"
                    + "script.async = true;\n"
                    + "script.onload = function(){\n"
                    + "script.onload = null;\n"
                    + "myScript('"
                    + String.valueOf(userId)
                    + "', '"
                    + String.valueOf(siteId)
                    + "');\n"
                    + "};\n"
                    + "script.src = 'https://www.sphynx.dev/generalScript.js';\n"
                    + "document.getElementsByTagName('head')[0].appendChild(script);\n"
                    + "}());\n"
                    + "</script>");
            return snippet.toString();
        } else {
            return "";
        }
    }

    /**
     * Update site url and description for a given site id.
     * @param siteId
     * @param url
     * @param description
     * @return true is site was updated
     */
    public boolean updateSite(Long siteId, String url, String description) {
        Site site = siteRepository.findBySiteId(siteId);
        if (site != null) {
            siteRepository.updateSiteInfo(siteId, url, description);
            return true;
        } else {
            return false;
        }
    }



}
