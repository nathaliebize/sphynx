package com.nathaliebize.sphynx.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import com.nathaliebize.sphynx.model.Session;
import com.nathaliebize.sphynx.model.Site;
import com.nathaliebize.sphynx.model.view.SubmitedSite;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.SiteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SitesControllerTest {
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
    SiteService siteService;
    
    @MockBean
    Site site;
    
    @MockBean
    SubmitedSite submitedSite;
    
    @Before
    public void setUp() {
      mockMvc = MockMvcBuilders
              .webAppContextSetup(webApplicationContext)
              .apply(springSecurity())
              .build();
      site.setId(1L);
      submitedSite.setUrl("url.com");
      submitedSite.setDescription("description");
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowSitesPage_newUser() throws Exception {
        when(siteService.getSiteList("email")).thenReturn(new ArrayList<Site>());
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SITES_CREATE.getPath()))
                .andExpect(authenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowSitesPage_withAuthorizedUser() throws Exception {
        ArrayList<Site> list = new ArrayList<Site>();
        Site site = new Site();
        list.add(site);
        
        when(siteService.getSiteList("email")).thenReturn(list);
        
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SITES_INDEX.getPath()))
                .andExpect(authenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowSiteDetailsPage_withAuthorizedUser() throws Exception {
        ArrayList<Session> sessionList = new ArrayList<Session>();
        when(siteService.getSessionList("email", this.site.getId())).thenReturn(sessionList);
        when(siteService.getSite("email", this.site.getId())).thenReturn(site);
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/" + this.site.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SESSIONS_INDEX.getPath()))
                .andExpect(authenticated());
    }
    
    @Test
    public void testShowSiteDetailsPage_withUnauthorizedUser() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/" + this.site.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(unauthenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowSiteDetailsPage_withAuthorizedUser_siteNotFound() throws Exception {
        ArrayList<Session> sessionList = new ArrayList<Session>();
        when(siteService.getSessionList("email", this.site.getId())).thenReturn(sessionList);
        when(siteService.getSite("email", this.site.getId())).thenReturn(null);
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/" + this.site.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(view().name(SiteMap.REDIRECT_ERROR_LOGOUT.getPath()))
                .andExpect(authenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowSiteDetailsPage_withAuthorizedUser_sessionListNotfound() throws Exception {
        when(siteService.getSessionList("email", this.site.getId())).thenReturn(null);
        when(siteService.getSite("email", this.site.getId())).thenReturn(site);
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/" + this.site.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(view().name(SiteMap.REDIRECT_ERROR_LOGOUT.getPath()))
                .andExpect(authenticated());;
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testShowCreatePage_authorizedUser() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/create")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SITES_CREATE.getPath()))
                .andExpect(authenticated());
    }
    
    @Test
    public void testShowCreatePage_UnauthorizedUser() throws Exception {
        when(siteService.saveSite(submitedSite, "email")).thenReturn(site);
        this.mockMvc.perform( MockMvcRequestBuilders
                .get("/sites/create")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(302))
                .andExpect(unauthenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testCreatePage_authorizedUser() throws Exception {
        when(siteService.saveSite(submitedSite, "email")).thenReturn(site);
        this.mockMvc.perform( MockMvcRequestBuilders
                .post("/sites/create")
                .param("url", "url.com")
                .param("description", "description"))
                .andExpect(status().isOk())
                .andExpect(authenticated());
    }
    
    @Test
    public void testCreatePage_unauthorizedUser() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .post("/sites/create")
                .param("url", "url.com")
                .param("description", "description"))
                .andExpect(status().is(302))
                .andExpect(unauthenticated());
    }
    
    @Test
    @WithMockUser(username = "email", password = "password", roles = "USER")
    public void testCreatePage_bindingError() throws Exception {
        this.mockMvc.perform( MockMvcRequestBuilders
                .post("/sites/create")
                .param("url", "not a url")
                .param("description", "description"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.SITES_CREATE.getPath()))
                .andExpect(authenticated());
    }
}
