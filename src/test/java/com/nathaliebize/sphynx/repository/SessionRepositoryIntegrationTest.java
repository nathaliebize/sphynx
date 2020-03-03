package com.nathaliebize.sphynx.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.Session;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SessionRepositoryIntegrationTest {
    @Autowired
    private SessionRepository sessionRepository;
    
    @Test
    public void testGetSessionList() {
        Session session = new Session();
        session.setId("sessionId2");
        session.setSiteId(1L);
        session.setUserId(2L);
        session.setDate(new Date(1583143328798L));
        session.setHost("mysite.com");
        sessionRepository.save(session);
        
        ArrayList<Session> renderedSessionList = sessionRepository.getSessionList(2L, 1L);
        
        assertNotNull(renderedSessionList);
        assertEquals("sessionId2", renderedSessionList.get(0).getId());
        
    }
    
    @Test
    public void testGetSession() {
        Session session = new Session();
        session.setId("sessionId3");
        session.setSiteId(1L);
        session.setUserId(2L);
        session.setDate(new Date(1583143328798L));
        session.setHost("mysite.com");
        sessionRepository.save(session);
        
        Session renderedSession = sessionRepository.getSession(2L, "sessionId3");
        
        assertNotNull(renderedSession);
        assertEquals("sessionId3", renderedSession.getId());
        assertEquals(new Date(1583143328798L), renderedSession.getDate());
    }
}
