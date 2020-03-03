package com.nathaliebize.sphynx.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathaliebize.sphynx.model.Site;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SiteRepositoryIntegrationTest {
    @Autowired
    private SiteRepository siteRepository;
    
    @Test
    public void testGetSiteList() {
        Site site = new Site();
        site.setUrl("myWebsite.com");
        site.setDescription("description");
        site.setUserId(2L);
        Site savedSite = siteRepository.save(site);
        
        ArrayList<Site> renderedSiteList = siteRepository.getSiteList(2L);
       
        assertNotNull(renderedSiteList);
        assertNotNull(renderedSiteList.get(0));
        assertEquals("myWebsite.com", renderedSiteList.get(0).getUrl());
    }
    
    @Test
    public void testGetSiteList_missingUrLSavedSite() {
        Site uncorrectSite = new Site();
        uncorrectSite.setId(3L);
        uncorrectSite.setDescription("description");
        uncorrectSite.setUserId(4L);
        Exception exception = null;
        
        try {
            siteRepository.save(uncorrectSite);
            siteRepository.getSiteList(4L);
        } catch (Exception e) {
            exception = e;
        }
        
        assertNotNull(exception);
    }
    
    @Test
    public void testGetSite() {
        Site site = new Site();
        site.setUrl("myWebsite.com");
        site.setDescription("description");
        site.setUserId(2L);
        Site savedSite = siteRepository.save(site);
        
        Site renderedSite = siteRepository.getSite(2L, savedSite.getId());
        
        assertNotNull(renderedSite);
        assertEquals(savedSite.getId(), renderedSite.getId());
        assertEquals(Long.valueOf(2L), renderedSite.getUserId());
    }
    
    @Test
    public void testGetSite_wrongId() {
        Site site = new Site();
        site.setUrl("myWebsite.com");
        site.setDescription("description");
        site.setUserId(2L);
        Site savedSite = siteRepository.save(site);
        
        Site renderedSite = siteRepository.getSite(1L, 1L);
        
        assertNull(renderedSite);
    }
    
    @Test
    public void testUpdateSiteSize() {
        Site previousSite = new Site();
        previousSite.setUrl("myWebsite.com");
        previousSite.setSize(1);
        previousSite.setDescription("description");
        previousSite.setUserId(4L);
        previousSite = siteRepository.save(previousSite);
        
        siteRepository.updateSiteSize(4L, previousSite.getId(), 3);
        
        Site updatedSite = siteRepository.getSite(4L, previousSite.getId());
    
        assertEquals(3, updatedSite.getSize());
    }
    
    @Test
    public void testUpdateSiteInfo() {
        Site previousSite = new Site();
        previousSite.setUrl("myWebsite.com");
        previousSite.setSize(1);
        previousSite.setDescription("description");
        previousSite.setUserId(4L);
        previousSite = siteRepository.save(previousSite);
        
        siteRepository.updateSiteInfo(4L, previousSite.getId(), "myUpdatedWebsite.com", "updatedDescription");
        
        Site updatedSite = siteRepository.getSite(4L, previousSite.getId());
    
        assertEquals("myUpdatedWebsite.com", updatedSite.getUrl());
        assertEquals("updatedDescription", updatedSite.getDescription());
    }
}
