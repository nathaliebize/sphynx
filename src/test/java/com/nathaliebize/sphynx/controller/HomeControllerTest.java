package com.nathaliebize.sphynx.controller;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nathaliebize.sphynx.repository.UserRepository;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.SphynxUserDetailsService;
import com.nathaliebize.sphynx.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
    private SphynxUserDetailsService sphynxUserDetailsService;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowHomePage() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.INDEX.getPath()));
    }
    
    @Test
    public void testShowHomePage_withIndexPath() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/index")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.INDEX.getPath()));
    }
    
    @Test
    public void testShowTermsPage() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/terms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.TERMS.getPath()));  
    }
    
    @Test
    public void testShowInfoPage() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/info")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.INFO.getPath()));  
    }
    
    @Test
    public void testIncorrectPath() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/random")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(unauthenticated());
    }
}
