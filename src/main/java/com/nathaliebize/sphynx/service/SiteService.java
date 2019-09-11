package com.nathaliebize.sphynx.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.ViewEvent;
import com.nathaliebize.sphynx.model.view.CreatedSite;
import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.repository.EventRepository;
import com.nathaliebize.sphynx.repository.SessionRepository;
import com.nathaliebize.sphynx.repository.SiteRepository;
import com.nathaliebize.sphynx.repository.UserRepository;
import com.nathaliebize.sphynx.repository.ViewEventRepository;

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
    
    @Autowired
    private ViewEventRepository viewEventRepository;
    
    public boolean saveSite(CreatedSite createdSite, String email) {
        Long userId = getUserId(email);
        if (userId != null) {
            Site site = new Site();
            site.setUser_id(userId);
            site.setUrl(createdSite.getUrl());
            site.setDescription(createdSite.getDescription());
            siteRepository.save(site);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Site> getSiteList(String email) {
        Long userId = getUserId(email);
        ArrayList<Site> siteList = null;
        if (userId != null) {
            siteList = siteRepository.findByUserId(userId);
        }
        return siteList;
    }
    
    public ArrayList<Session> getSessionList(String email, Long siteId) {
        Long userId = getUserId(email);
        ArrayList<Session> sessionList = null;
        if (userId != null) {
            sessionList = sessionRepository.getSessionList(userId, siteId);
        }
        for (Session session: sessionList) {
            if (session.getStatus().equals("STARTED")) {
                session.setStatus(this.process(session));
            }
        }
        return sessionList;
    }
    
    private String process(Session session) {
        ArrayList<Event> eventList = getEventList(session.getUserId(), session.getSessionId());
        ArrayList<ViewEvent> parsedEventList = new ArrayList<ViewEvent>();
        Date dateLastEvent = null;
        for (Event event : eventList) {
            if (dateLastEvent == null || (dateLastEvent.compareTo(event.getTimestamp()) < 0)) {
                dateLastEvent = event.getTimestamp();
            }
        }
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        if (true) {
//        if (dateLastEvent.compareTo(yesterday) < 0) {
            boolean eventRunning = false;
            for (int i = 0, j = 0; i < eventList.size(); i++) {
                if (eventRunning) {
                    while (i < eventList.size() - 1 && eventList.get(i).getType().equals(eventList.get(i + 1).getType())) {
                        i ++;
                    }
                    eventRunning = false;
                    parsedEventList.get(j - 1).setTimeStop(eventList.get(((i+1) < eventList.size()) ? i + 1 : i).getTimestamp());
                } else {
                    ViewEvent parsedEvent = new ViewEvent(eventList.get(i));
                    parsedEventList.add(parsedEvent);
                    if (i == 0) {
                        parsedEventList.get(j).setGroupType(0);
                    }
                    j++;
                    if (eventList.get(i).getGroupType() == 2 && i + 1 < eventList.size() && eventList.get(i).getType().equals(eventList.get(i + 1).getType())) {
                        eventRunning = true;
                    }
                }
            }
            for (ViewEvent viewEvent : parsedEventList) {
                viewEventRepository.save(viewEvent);
            }
            return "ENDED";
        }
        return "STARTED";
    }
    
    public ArrayList<Event> getEventList(Long userId, Long sessionId) {
        return eventRepository.getEventList(userId, sessionId);
    }

    public ArrayList<Event> getEventList(String email, Long sessionId) {
        Long userId = getUserId(email);
        ArrayList<Event> eventList = new ArrayList<Event>();
        if (userId != null) {
            eventList = eventRepository.getEventList(userId, sessionId);
        }
        return eventList;
    }
    
    public ArrayList<ViewEvent> getViewEventList(String email, Long sessionId) {
        Long userId = getUserId(email);
        ArrayList<ViewEvent> viewEventList = new ArrayList<ViewEvent>();
        if (userId != null) {
            viewEventList = viewEventRepository.getViewEventList(userId, sessionId);
        }
        return viewEventList;
    }
    
    private Long getUserId(String email) {
        User user = userRepository.findByEmail(email);
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

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

    public void saveSession(RecordedSession jsonString) {
        Session session = new Session();
        session.setUserId(jsonString.getUserId());
        session.setSiteId(jsonString.getSiteId());
        session.setSessionId(jsonString.getSessionId());
        sessionRepository.save(session);
        
    }

}
