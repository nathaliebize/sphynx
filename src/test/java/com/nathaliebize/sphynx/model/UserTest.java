package com.nathaliebize.sphynx.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.nathaliebize.sphynx.model.User;

public class UserTest {

    @Test
    public void testGenerateRegistrationKey_randomized() {
        User user = new User();
        String previousKey = user.getRegistrationKey();
        
        String nextKey = user.generateRegistrationKey();
                
        assertNotEquals(previousKey, nextKey);
    }
    
    @Test
    public void testGenerateRegistrationKey_neverNull() {
        User user = new User();
        
        String key = user.generateRegistrationKey();
        
        assertNotNull(key);
    }
    
    @Test
    public void testUser_constructor() {     
        User user = new User("email", "password");
        
        assertNull(user.getId());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());
        assertNotNull(user.getRegistrationKey());
        assertEquals("UNVERIFIED", user.getRegistrationStatus());   
    }
}
