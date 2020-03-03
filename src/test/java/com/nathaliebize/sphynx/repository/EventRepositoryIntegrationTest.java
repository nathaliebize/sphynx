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

import com.nathaliebize.sphynx.model.Event;
import com.nathaliebize.sphynx.model.EventType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryIntegrationTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testGetEventList() {
        Event event_1 = new Event();
        event_1.setSessionId("sessionId1");
        event_1.setSiteId(1L);
        event_1.setUserId(2L);
        event_1.setType(EventType.START);
        event_1.setDate(new Date(1583143328798L));
        event_1.setPath("path1");
        eventRepository.save(event_1);
        Event event_2 = new Event();
        event_2.setSessionId("sessionId2");
        event_2.setSiteId(3L);
        event_2.setUserId(4L);
        event_2.setType(EventType.CLICK);
        event_2.setDate(new Date(1583143928798L));
        event_2.setPath("path2");
        eventRepository.save(event_2);
        
        
        ArrayList<Event> renderedEventList = (ArrayList<Event>) eventRepository.findAll();
        
        assertNotNull(renderedEventList);
        assertEquals("sessionId1", renderedEventList.get(0).getSessionId());
        assertEquals("sessionId2", renderedEventList.get(1).getSessionId());
        assertEquals(Long.valueOf(1L), renderedEventList.get(0).getSiteId());
        assertEquals(Long.valueOf(3L), renderedEventList.get(1).getSiteId());
        assertEquals(Long.valueOf(2L), renderedEventList.get(0).getUserId());
        assertEquals(Long.valueOf(4L), renderedEventList.get(1).getUserId());
        assertEquals(EventType.START, renderedEventList.get(0).getType());
        assertEquals(EventType.CLICK, renderedEventList.get(1).getType());
        assertEquals(new Date(1583143328798L), renderedEventList.get(0).getDate());        
        assertEquals(new Date(1583143928798L), renderedEventList.get(1).getDate());
        assertEquals("path1", renderedEventList.get(0).getPath());
        assertEquals("path2", renderedEventList.get(1).getPath());
   }
}
