package com.nathaliebize.sphynx.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class EventTest {
    private Event event;
    
    @Before
    public void setUp() {
        event = new Event();
        event.setDate(new Date(1583143328798L));
    }
    
    @Test
    public void testGetDuration_25h() {
        String duration = event.getDuration(new Date(1583233328798L));
        
        assertEquals("24h+", duration);
    }
    
    @Test
    public void testGetDuration_errorChronologicalOrder() {
        String duration = event.getDuration(new Date(1583142728798L));
        
        assertEquals("###", duration);
    }
    
    @Test
    public void testGetDuration_2hours() {
        String duration = event.getDuration(new Date(1583150528798L));
        
        assertEquals("2h00", duration);
    }
    
    @Test
    public void testGetDuration_1hour12() {
        String duration = event.getDuration(new Date(1583147648798L));
        
        assertEquals("1h12", duration);
    }
    
    @Test
    public void testGetDuration_10minutes() {
        String duration = event.getDuration(new Date(1583143928798L));
        
        assertEquals("10 min", duration);
    }
    
    @Test
    public void testGetDuration_seconds() {
        String duration = event.getDuration(new Date(1583143347798L));
        
        assertEquals("19 sec", duration);
    }
}
