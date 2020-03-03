package com.nathaliebize.sphynx.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.EventType;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.RecordedEvent;
import com.nathaliebize.sphynx.model.view.RecordedSession;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.model.view.SubmitedSite;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceIntegrationTest {
    @TestConfiguration
    static class ServiceIntegrationTestContextConfiguration {
  
        @Bean
        public DataService dataService() {
            return new DataService();
        }
        
        @Bean
        public UserService userService() {
            return new UserService();
        }
        
        @Bean
        public SiteService siteService() {
            return new SiteService();
        }    
    }   
    
    @Autowired
    private DataService dataService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SiteService siteService;
    
    @Test
    public void testSaveEventAndSaveSession() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("email@email.com");
        registerUser.setPassword("password");
        registerUser.setConfirmedPassword("password");
        registerUser.setAcceptedTerms(true);
        
        User user = userService.registerNewUser(registerUser);
        
        SubmitedSite submitedSite = new SubmitedSite();
        submitedSite.setDescription("This is my site");
        submitedSite.setUrl("mywebsite.com");
        
        Site site = siteService.saveSite(submitedSite, "email@email.com");

        RecordedSession recordedSession = new RecordedSession();
        recordedSession.setDate(new Date(1583212007805L));
        recordedSession.setHost("mywebsite.com");
        recordedSession.setSessionId("sessionId");
        recordedSession.setSiteId(site.getId());
        recordedSession.setUserId(user.getId());
        
        dataService.saveSession(recordedSession);
        
        RecordedEvent recordedEvent = new RecordedEvent();
        recordedEvent.setUserId(user.getId());
        recordedEvent.setSessionId("sessionId");
        recordedEvent.setPath("https://mywebsite.com");
        recordedEvent.setDate(new Date(1583213207805L));
        recordedEvent.setSiteId(site.getId());
        recordedEvent.setType(EventType.CLICK);
        
        dataService.saveEvent(recordedEvent);

        ArrayList<Event> renderedEventList = siteService.getEventList("email@email.com", "sessionId");
        
        assertNotNull(renderedEventList);
        assertEquals(site.getId(), renderedEventList.get(0).getSiteId());
        assertNotNull(renderedEventList.get(0));
    }
}
