package com.nathaliebize.sphynx.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.EventType;
import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.SiteService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SessionsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
        
    @MockBean
    SiteService siteService;
    
    @Before
    public void setUp() {
      mockMvc = MockMvcBuilders
              .webAppContextSetup(webApplicationContext)
              .apply(springSecurity())
              .build();
    }
    
    @Test
    @WithMockUser(username = "email@email.com", password = "password", roles = "USER")
    public void testShowSessionTimelinePage() throws Exception {
        Session session = new Session();
        session.setId("1234");
        session.setSiteId(2222L);
        session.setDate(new Date(1583140690530L));
        
        ArrayList<Event> eventList = new ArrayList<Event>();
        Event event = new Event();
        event.setSessionId("1234");
        event.setSiteId(2222L);
        event.setType(EventType.CLICK);
        event.setDate(new Date(1583140690530L));
        eventList.add(event);
        
        Event event2 = new Event();
        event2.setSessionId("1234");
        event2.setSiteId(2222L);
        event2.setType(EventType.LEAVE_TAB);
        event.setDate(new Date(1583141290530L));
        eventList.add(event2);
        
        Site site = new Site();
        site.setId(2222L);
        site.setUrl("test.com");
        site.setDescription("This is a test");
       
        when(siteService.getEventList("email@email.com", "1234")).thenReturn(eventList);
        when(siteService.getSession("email@email.com", "1234")).thenReturn(session);
        when(siteService.getSite("email@email.com", 2222L)).thenReturn(site);
                
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sessions/1234")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SESSIONS_TIMELINE.getPath()));
    }

}
