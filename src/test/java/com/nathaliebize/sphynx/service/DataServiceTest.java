package com.nathaliebize.sphynx.service;


import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.EventType;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.repository.EventRepository;
import com.nathaliebize.sphynx.repository.SessionRepository;
import com.nathaliebize.sphynx.repository.SiteRepository;

@RunWith(SpringRunner.class)
public class DataServiceTest {
    @TestConfiguration
    static class DataServiceTestContextConfiguration {
  
        @Bean
        public DataService dataService() {
            return new DataService();
        }
    }   
    
    @Autowired
    private DataService dataService;
    
    @MockBean
    private SiteRepository siteRepository;
    
    @MockBean
    private SessionRepository sessionRepository;
    
    @MockBean
    private EventRepository eventRepository;
    
    @Test
    public void testSaveEvent() {
        Exception exception = null;
        try {
            RecordedEvent recordedEvent = new RecordedEvent();
            recordedEvent.setSessionId("sessionId");
            recordedEvent.setSiteId(1L);
            recordedEvent.setUserId(2L);
            recordedEvent.setType(EventType.START);
            recordedEvent.setDate(new Date(1583141661826L));
            recordedEvent.setTarget("button");
            recordedEvent.setPath("path1");
            
            dataService.saveEvent(recordedEvent);
        
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);                
    }
    
    @Test
    public void testSaveEvent_noTarget() {
        Session session = new Session();
        session.setId("sessionId");
        session.setSiteId(1L);
        session.setUserId(2L);
        when(sessionRepository.getSession(2L, "sessionId")).thenReturn(session);
        Site site = new Site();
        site.setUrl("path1");
        when(siteRepository.getSite(2L, 1L)).thenReturn(site);
        Exception exception = null;
        try {
            RecordedEvent recordedEvent = new RecordedEvent();
            recordedEvent.setSessionId("sessionId");
            recordedEvent.setSiteId(1L);
            recordedEvent.setUserId(2L);
            recordedEvent.setType(EventType.START);
            recordedEvent.setDate(new Date(1583141661826L));
            recordedEvent.setPath("path1");
            
            dataService.saveEvent(recordedEvent);
        
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
    }
    
    @Test
    public void testSaveEvent_noMatchingSession() {
        when(sessionRepository.getSession(2L, "sessionId")).thenReturn(null);
        Exception exception = null;
        try {
            RecordedEvent recordedEvent = new RecordedEvent();
            recordedEvent.setSessionId("sessionId");
            recordedEvent.setSiteId(1L);
            recordedEvent.setUserId(2L);
            recordedEvent.setType(EventType.START);
            recordedEvent.setDate(new Date(1583141661826L));
            recordedEvent.setTarget("button");
            recordedEvent.setPath("path1");
            
            dataService.saveEvent(recordedEvent);
        
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);                
    }
}
