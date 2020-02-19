package com.nathaliebize.sphynx.service;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.CreatedSite;
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
     * Saves a site into the database. It takes a createdSite object and convert it to
     * a Site object. 
     * @param createdSite
     * @param email
     * @return true if user could be retrive, false otherwise.
     */
    public boolean saveSite(CreatedSite createdSite, String email) {
        Long userId = getUserId(email);
        if (userId != null) {
            Site site = new Site();
            site.setUserId(userId);
            site.setUrl(createdSite.getUrl());
            site.setDescription(createdSite.getDescription());
            siteRepository.save(site);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Saves an event to database.
     * @param jsonString
     */
    public void saveEvent(RecordedEvent jsonString) {
        Event event = new Event();
        event.setSessionId(jsonString.getSessionId());
        event.setSiteId(jsonString.getSiteId());
        event.setUserId(jsonString.getUserId());
        event.setTimestamp(jsonString.getTimestamp());
        event.setType(jsonString.getType());
        event.setPath(jsonString.getPath());
        event.setTarget(jsonString.getTarget());
        event.setGroupType(jsonString.getGroupType());
        eventRepository.save(event);
    }
    
//    public void closeSession(RecordedEvent jsonString) {
//        Session session = sessionRepository.findBySessionId(jsonString.getSessionId());
//
//        if (getEventList(session.getUserId(), session.getSessionId()).size() < 4) {
//            sessionRepository.delete(session);
//        } else {
//            createViewEventList(session);
//            session.setStatus("ENDED");
//            sessionRepository.changeSessionStatus("ENDED", jsonString.getSessionId());
//            processData(session.getUserId());
//        }
//    }
    
    /**
     * Saves a session to database.
     * @param jsonString
     */
    public void saveSession(RecordedSession jsonString) {
        Session session = new Session();
        session.setUserId(jsonString.getUserId());
        session.setSiteId(jsonString.getSiteId());
        session.setSessionId(jsonString.getSessionId());
        session.setStartTime(jsonString.getStartTime());
        sessionRepository.save(session);
        
    }
    
    /**
     * Returns a list of Site for one given email addresse.
     * @param email
     * @return
     */
    public ArrayList<Site> getSiteList(String email) {
        Long userId = getUserId(email);
        ArrayList<Site> siteList = null;
        if (userId != null) {
            siteList = siteRepository.findByUserId(userId);
        }
        return siteList;
    }
    /**
     * Returns a list of Site for one given user id.
     * @param userId
     * @return
     */
    public ArrayList<Site> getSiteList(Long userId) {
        return siteRepository.findByUserId(userId);
    }

    /**
     * Returns a list of Session for one given userId and site id pair.
     * @param userId
     * @param id
     * @return
     */
    public ArrayList<Session> getSessionList(@NotNull Long userId, Long id) {
        return sessionRepository.getSessionList(userId, id);
    }
    
    /**
     * Returns a list of Session for one given email address and site id pair.
     * @param email
     * @param siteId
     * @return
     */
    public ArrayList<Session> getSessionList(String email, Long siteId) {
        Long userId = getUserId(email);
        ArrayList<Session> sessionList = null;
        if (userId != null) {
            sessionList = sessionRepository.getSessionList(userId, siteId);
        }
        return sessionList;
    }
    
//    public ArrayList<ViewEvent> getViewEventList(String email, Long sessionId) {
//        Long userId = getUserId(email);
//        ArrayList<ViewEvent> viewEventList = new ArrayList<ViewEvent>();
//        if (userId != null) {
//            viewEventList = viewEventRepository.getViewEventList(userId, sessionId);
//        }
//        return viewEventList;
//    }
    
    /**
     * Returns a list of Events for a given session, using the session id and user id.
     * @param userId
     * @param sessionId
     * @return
     */
    public ArrayList<Event> getEventList(Long userId, Long sessionId) {
        return eventRepository.getEventList(userId, sessionId);
    }

    /**
     * Returns a list of Events for a given session, using an email address and session id.
     * @param email
     * @param sessionId
     * @return
     */
    public ArrayList<Event> getEventList(String email, Long sessionId) {
        Long userId = getUserId(email);
        ArrayList<Event> eventList = new ArrayList<Event>();
        if (userId != null) {
            eventList = eventRepository.getEventList(userId, sessionId);
        }
        return eventList;
    }
    
    /**
     * Returns the user id for a given email address.
     * @param email
     * @return
     */
    private Long getUserId(String email) {
        User user = userRepository.findByEmail(email);
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

    /**
     * Return a Site from database given an site id.
     * @param id
     * @return
     */
    public Site findBySiteId(Long id) {
        Site site = siteRepository.findBySiteId(id);
        return site;
    }
    
//    /**
//     * Iterates throught the events' session and create a viewEvent list to parse the information.
//     * @param session
//     */
//    private void createViewEventList(Session session) {
//        ArrayList<Event> eventList = getEventList(session.getUserId(), session.getSessionId());
//        ArrayList<ViewEvent> viewEventList = new ArrayList<ViewEvent>();
//        boolean eventRunning = false;
//        for (int i = 0, j = 0; i < eventList.size(); i++) {
//            if (eventRunning) {
//                while (i < eventList.size() - 1 && eventList.get(i).getType().equals(eventList.get(i + 1).getType())) {
//                    i ++;
//                }
//                eventRunning = false;
//                viewEventList.get(j - 1).setTimeStop(eventList.get(((i+1) < eventList.size()) ? i + 1 : i).getTimestamp());
//            } else {
//                ViewEvent parsedEvent = new ViewEvent(eventList.get(i));
//                viewEventList.add(parsedEvent);
//                if (i == 0) {
//                    viewEventList.get(j).setGroupType(0);
//                }
//                j++;
//                if (eventList.get(i).getGroupType() == 2 && i + 1 < eventList.size() && eventList.get(i).getType().equals(eventList.get(i + 1).getType())) {
//                    eventRunning = true;
//                }
//            }
//        }
//        for (ViewEvent viewEvent : viewEventList) {
//            viewEventRepository.save(viewEvent);
//        }
//    }

    /**
     * Update size (number of session) of a given site.
     * @param site
     */
    private void updateSiteSize(Site site) {
        int siteSize = sessionRepository.getSessionList(site.getUserId(), site.getId()).size();
        if (siteSize != site.getSize()) {
            siteRepository.updateSiteSize(site.getId(), siteSize);
        }
    }
    
    /**
     * Process the data in database to update number of session, clean up aborded session.
     */
    public void processData(Long userId) {
        ArrayList<Site> siteList = getSiteList(userId);
        if (siteList != null) {
            for (Site site : siteList) {
                updateSiteSize(site);
            }
        }
    }

}
