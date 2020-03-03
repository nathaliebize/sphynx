package com.nathaliebize.sphynx.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.SubmitedSite;
import com.nathaliebize.sphynx.repository.EventRepository;
import com.nathaliebize.sphynx.repository.SessionRepository;
import com.nathaliebize.sphynx.repository.SiteRepository;
import com.nathaliebize.sphynx.repository.UserRepository;

@RunWith(SpringRunner.class)
public class SiteServiceTest {

    @TestConfiguration
    static class SiteServiceTestContextConfiguration {
  
        @Bean
        public SiteService siteService() {
            return new SiteService();
        }
    }   
    
    @Autowired
    private SiteService siteService;
    
    @MockBean
    private SiteRepository siteRepository;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private SessionRepository sessionRepository;
    
    @MockBean
    private EventRepository eventRepository;
    
    private SubmitedSite submitedSite = new SubmitedSite();
    private String principalName = "email";
    private User user = new User("email", "password");
    private Site site = new Site();
    private ArrayList<Site> siteList = new ArrayList<>();
    private Session session = new Session();
    private ArrayList<Session> sessionList = new ArrayList<>();
    private Event event = new Event();
    private ArrayList<Event> eventList = new ArrayList<>();

    @Before 
    public void setUp() {
        submitedSite.setUrl("www.sphynx.dev");
        submitedSite.setDescription("This is the description");
        user.setId(2L);
        site.setId(3L);
        site.setUserId(2L);
        site.setUrl("myWebsite.com");
        site.setDescription("Description");
        siteList.add(site);
        session.setUserId(2L);
        session.setId("sessionId");
        sessionList.add(session);
        event.setSiteId(3L);
        event.setUserId(2L);
        eventList.add(event);
        when(userRepository.findByEmail(principalName)).thenReturn(user);
        when(siteRepository.getSite(user.getId(), site.getId())).thenReturn(site);
        when(siteRepository.getSiteList(user.getId())).thenReturn(siteList);
        when(sessionRepository.getSession(2L, "sessionId")).thenReturn(session);
        when(sessionRepository.getSessionList(user.getId(), site.getId())).thenReturn(sessionList);
        when(eventRepository.getEventList(user.getId(), session.getId())).thenReturn(eventList);
    }
    
    @Test
    public void testSaveSite() {
        Site renderedSite = siteService.saveSite(submitedSite, principalName);
    
        assertNotNull(renderedSite);
        assertEquals("www.sphynx.dev", renderedSite.getUrl());
        assertEquals("This is the description", renderedSite.getDescription());
    }
    
    @Test
    public void testSaveSite_nullParam() {
        Site renderedSite = siteService.saveSite(null, null);
    
        assertNull(renderedSite);
    }
    
    @Test
    public void testSaveSite_userNotFound() {
        when(userRepository.findByEmail(principalName)).thenReturn(null);
        
        Site renderedSite  = siteService.saveSite(submitedSite, principalName);
    
        assertNull(renderedSite.getId());  
    }
    
    @Test
    public void testGetSiteList() {        
        ArrayList<Site> renderedSiteList = siteService.getSiteList("email");
        
        assertNotNull(renderedSiteList);
        assertEquals(siteList, renderedSiteList);
    }
    
    @Test
    public void testGetSiteList_nullParam() {        
        ArrayList<Site> renderedSiteList = siteService.getSiteList(null);
        
        assertNull(renderedSiteList);
    }
    
    @Test
    public void testGetSiteList_userNotFound() {
        when(userRepository.findByEmail(principalName)).thenReturn(null);

        ArrayList<Site> renderedSiteList = siteService.getSiteList("email");
        
        assertTrue(renderedSiteList.isEmpty());
    }
    
    @Test
    public void testGetSessionList() {
        ArrayList<Session> renderedSessionList = siteService.getSessionList(principalName, site.getId());
        
        assertNotNull(renderedSessionList);
        assertEquals(sessionList, renderedSessionList);
    }
    
    @Test
    public void testGetSessionList_userNotFound() {
        when(userRepository.findByEmail(principalName)).thenReturn(null);

        ArrayList<Session> renderedSessionList = siteService.getSessionList(principalName, site.getId());
        
        assertTrue(renderedSessionList.isEmpty());
    }
    
    @Test
    public void testGetSessionList_nullParam() {
        ArrayList<Session> renderedSessionList = siteService.getSessionList(null, null);
        
        assertNull(renderedSessionList);
    }
    
    @Test
    public void testGetSessionList_wrongSiteId() {
        ArrayList<Session> renderedSessionList = siteService.getSessionList(principalName, 4L);
        
        assertNotNull(renderedSessionList);
        assertTrue(renderedSessionList.isEmpty());
    }
    
    @Test
    public void getEventList() {
        ArrayList<Event> renderedEventList = siteService.getEventList(principalName, session.getId());
        
        assertNotNull(renderedEventList);
        assertEquals(eventList, renderedEventList);
    }
    
    @Test
    public void getEventList_nullParam() {
        ArrayList<Event> renderedEventList = siteService.getEventList(null, null);
        
        assertNull(renderedEventList);
    }
    
    @Test
    public void getEventList_userNotFound() {
        when(userRepository.findByEmail(principalName)).thenReturn(null);

        ArrayList<Event> renderedEventList = siteService.getEventList(principalName, session.getId());
        
        assertNotNull(renderedEventList);
        assertNotEquals(eventList, renderedEventList);
    }
    
    @Test
    public void testGetSite() {
        Site renderedSite = siteService.getSite(principalName, 3L);

        assertNotNull(renderedSite);
        assertEquals(site, renderedSite);
    }
    
    @Test
    public void testGetSite_nullParam() {
        Site renderedSite = siteService.getSite(null, null);

        assertNull(renderedSite);
    }
    
    @Test
    public void testGetSite_wrongPrincipalName() {
        Site renderedSite = siteService.getSite("wrong", 3L);

        assertNull(renderedSite);
    }
    
    @Test
    public void testGetSite_wrongSiteId() {
        Site renderedSite = siteService.getSite(principalName, 2L);

        assertNull(renderedSite);
    }
    
    @Test
    public void testGetSession() {
        Session renderedSession = siteService.getSession(principalName, "sessionId");
        
        assertNotNull(renderedSession);
        assertEquals(session, renderedSession);
    }
    
    @Test
    public void testGetSession_nullParam() {
        Session renderedSession = siteService.getSession(null, null);
        
        assertNull(renderedSession);
    }
    
    @Test
    public void testGetSession_wrongPrincipalName() {
        Session renderedSession = siteService.getSession("wrong", "sessionId");
        
        assertNull(renderedSession);
    }
    
    @Test
    public void testGetSession_wrongSessionId() {
        Session renderedSession = siteService.getSession(principalName, "wrong");
        
        assertNull(renderedSession);
    }
    
    @Test
    public void testDeleteSite() {
        boolean isDeleted = siteService.deleteSite(principalName, site.getId());
        
        assertTrue(isDeleted);
    }
    
    @Test
    public void testDeleteSite_nullParam() {
        boolean isDeleted = siteService.deleteSite(null, null);
        
        assertFalse(isDeleted);
    }
    
    @Test
    public void testDeleteSite_sessionListEmpty() {
        sessionList = new ArrayList<>();
        when(sessionRepository.getSessionList(user.getId(), site.getId())).thenReturn(sessionList);

        ArrayList<Session> renderedSessionList = siteService.getSessionList(principalName, site.getId()); 
        boolean isDeleted = siteService.deleteSite(principalName, site.getId());
        
        assertTrue(isDeleted);
        assertTrue(renderedSessionList.isEmpty());
    }
    
    @Test
    public void testDeleteSite_nullSession() {
        sessionList = new ArrayList<>();
        sessionList.add(null);
        when(sessionRepository.getSessionList(user.getId(), site.getId())).thenReturn(sessionList);

        ArrayList<Session> renderedSessionList = siteService.getSessionList(principalName, site.getId()); 
        boolean isDeleted = siteService.deleteSite(principalName, site.getId());
        
        assertFalse(isDeleted);
        assertFalse(renderedSessionList.isEmpty());
        assertNull(renderedSessionList.get(0));
    }
    
    @Test
    public void testDeleteSite_nullEvent() {
        eventList = new ArrayList<>();
        eventList.add(null);
        when(eventRepository.getEventList(user.getId(), session.getId())).thenReturn(eventList);

        ArrayList<Event> renderedEventList = siteService.getEventList(principalName, session.getId()); 
        boolean isDeleted = siteService.deleteSite(principalName, site.getId());
        
        assertTrue(isDeleted);
        assertFalse(renderedEventList.isEmpty());
        assertNull(renderedEventList.get(0));
    }
    
    @Test
    public void testDeleteSession() {
        boolean isDeleted = siteService.deleteSession(principalName, session.getId());
        
        assertTrue(isDeleted);
    }
    
    @Test
    public void testDeleteSession_nullParam() {
        boolean isDeleted = siteService.deleteSession(null, null);
        
        assertFalse(isDeleted);
    }
    
    @Test
    public void testGenerateCodeSnippet() {
        String codeSnippet = "<script type='text/javascript'>\n"
                + "(function() {\n"
                + "let script = document.createElement('script');\n"
                + "script.type = 'text/javascript';\n"
                + "script.async = true;\n"
                + "script.onload = function(){\n"
                + "script.onload = null;\n"
                + "myScript('2', '3');\n"
                + "};\n"
                + "script.src = 'https://www.sphynx.dev/generalScript.js';\n"
                + "document.getElementsByTagName('head')[0].appendChild(script);\n"
                + "}());\n"
                + "</script>";
                
        String renderedCodeSnippet = siteService.generateCodeSnippet(principalName, 3L);
        
        assertEquals(codeSnippet, renderedCodeSnippet);
    }
    
    @Test
    public void testGenerateCodeSnippet_nullParam() {
        String renderedCodeSnippet = siteService.generateCodeSnippet(null, null);
        
        assertEquals("", renderedCodeSnippet);
    }
    
    @Test
    public void testGenerateCodeSnippet_siteNotFound() {
        when(siteRepository.getSite(user.getId(), site.getId())).thenReturn(null);

        String renderedCodeSnippet = siteService.generateCodeSnippet(principalName, 3L);
        
        assertEquals("", renderedCodeSnippet);
    }
    
    @Test
    public void testUpdateSite() {        
        boolean isUpdated = siteService.updateSite(principalName, 3L, "myUpdatedWebsite.com", "updatedDescription");
        
        assertTrue(isUpdated);
    }
    
    @Test
    public void testUpdateSite_nullParam() {        
        boolean isUpdated = siteService.updateSite(principalName, 3L, null, "updatedDescription");
        
        assertFalse(isUpdated);
    }
}
