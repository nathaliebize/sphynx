package com.nathaliebize.sphynx.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.SubmitedSite;
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
     * @return the site if user could be retrieve, null otherwise.
     */
    public Site saveSite(SubmitedSite submitedSite, String principalName) {
        if (submitedSite == null || principalName == null) {
            return null;
        }
        Site site = new Site();
        site.setUserId(getUserId(principalName));
        site.setUrl(submitedSite.getUrl());
        site.setDescription(submitedSite.getDescription());
        siteRepository.save(site);
        return site;
    }

    /**
     * Gets an arraylist of sites for a given principal user's name.
     * @param principalName
     * @return arrayList of sites
     */
    public ArrayList<Site> getSiteList(String principalName) {
        if (principalName == null) {
            return null;
        }
        return siteRepository.getSiteList(getUserId(principalName));
    }
    
    /**
     * Gets an arraylist of sites for one given user id.
     * @param userId
     * @return list of sites
     */
    private ArrayList<Site> getSiteList(Long userId) {
        if (userId == null) {
            return null;
        }
        return siteRepository.getSiteList(userId);
    }
    
    /**
     * Gets an arraylist of sessions for a given principal user's name and site id pair.
     * @param principalName
     * @param siteId
     * @return list of sessions
     */
    public ArrayList<Session> getSessionList(String principalName, Long siteId) {
        if (principalName == null || siteId == null) {
            return null;
        }
        return sessionRepository.getSessionList(getUserId(principalName), siteId);
    }
    
    /**
     * Returns an arraylist of events for a given session, using the principal's name and the session id.
     * @param principalName
     * @param siteId
     * @return list of events. If no user found, list will be empty.
     */
    public ArrayList<Event> getEventList(String principalName, String sessionId) {
        if (principalName == null || sessionId == null) {
            return null;
        }
        return eventRepository.getEventList(getUserId(principalName), sessionId);
    }    

    /**
     * Return a site from database for a given site id.
     * @param site id
     * @return site
     */
    public Site getSite(String principalName, Long siteId) {
        if (principalName == null || siteId == null) {
            return null;
        }    
        return siteRepository.getSite(getUserId(principalName), siteId);
    }
    
    /**
     * Return a session from database for a given session id.
     * @param sessionId
     * @return session
     */
    public Session getSession(String principalName, String sessionId) {
        if (principalName == null || sessionId == null) {
            return null;
        }
        return sessionRepository.getSession(getUserId(principalName), sessionId);
    }
    
    /**
     * Update in database the number of session of each sites of a given user.
     * @param principalName
     */
    public void updateSiteSize(String principalName) {
        try {
            ArrayList<Site> siteList = getSiteList(getUserId(principalName));
            if (siteList == null) {
                return;
            }
            for (Site site : siteList) {
                ArrayList<Session> sessionList = sessionRepository.getSessionList(site.getUserId(), site.getId());
                int siteSize = sessionList.size();
                if (siteSize != site.getSize()) {
                    siteRepository.updateSiteSize(getUserId(principalName), site.getId(), siteSize);
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Deletes a site and its related sessions for a given siteId and a given user from the databse.
     * @param principalName
     * @param siteId
     * @return true if site was deleted
     */
    public boolean deleteSite(String principalName, Long siteId) {
        if (principalName == null || siteId == null) {
            return false;
        }
        Long userId = getUserId(principalName);
        Site site = siteRepository.getSite(userId, siteId);
        siteRepository.delete(site);
        ArrayList<Session> sessionList = getSessionList(principalName, siteId);
        if (sessionList.isEmpty()) {
            return true;
        }
        for (Session session : sessionList) {
            if(!deleteSession(session)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean deleteSession(Session session) {
        if (session == null) {
            return false;
        }
        ArrayList<Event> eventList = eventRepository.getEventList(session.getUserId(), session.getId());
        for (Event event : eventList) {
            eventRepository.delete(event);
        }
        sessionRepository.delete(session);
        return true;
    }
    
    /**
     * Deletes all sessions for a given site id and a given user from the databse.
     * @param principalName
     * @param sessionId
     */
    public boolean deleteSession(String principalName, String sessionId) {
        if (principalName == null || sessionId == null) {
            return false;
        }
        Session session = sessionRepository.getSession(getUserId(principalName), sessionId);
        return deleteSession(session);
    }
   
    /**
     * Generates the code snippet with the correct user id and site id for a given user and a given site.
     * @param principalName
     * @param siteId
     * @return the code snippet.
     */
    public String generateCodeSnippet(String principalName, Long siteId) {
        if (principalName == null || siteId == null) {
            return "";
        }
        Long userId = getUserId(principalName);
        Site site = siteRepository.getSite(userId, siteId);
        if (site == null) {
            return "";
        }
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
                + "script.src = 'https://www.sphynx.dev/general-script';\n"
                + "document.getElementsByTagName('head')[0].appendChild(script);\n"
                + "}());\n"
                + "</script>");
        return snippet.toString();
    }

    /**
     * Update site url and description for a given site id.
     * @param siteId
     * @param url
     * @param description
     * @return true is site was updated
     */
    public boolean updateSite(String principalName, Long siteId, String url, String description) {
        if (principalName == null || siteId == null || url == null || description == null) {
            return false;
        }
        siteRepository.updateSiteInfo(getUserId(principalName), siteId, url, description);
        return true;
    }
    
    /**
     * Gets the user id for a given principal user's name.
     * @param principalName
     * @return user id or null
     */
    private Long getUserId(String principalName) {
        User user = userRepository.findByEmail(principalName);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }   
}
