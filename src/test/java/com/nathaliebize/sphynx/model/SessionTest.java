package com.nathaliebize.sphynx.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class SessionTest {
    @Test
    public void testSetStartTime() {
        Date date = new Date();
        Session session = new Session();
        session.setId("sessionId");
        session.setSiteId(1L);
        session.setUserId(2L);
        session.setDate(date);
        session.setHost("mysite.com");
        
        Date renderedDate = session.getDate();
        
        assertEquals(date, renderedDate);
    }
}
