package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.Site;

/**
 * JPA Repository that connects to the sites table.
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    /**
     * Retrieves a list of sites given a user id.
     * @param userId
     * @return list of sites
     */
    @Query("select s from Site s where s.userId = ?1 order by size desc")
    ArrayList<Site> getSiteList(Long userId);
    
    /**
     * Retrieves a site given a user id and site id.
     * @param userId
     * @param siteId
     * @return site
     */
    @Query("select s from Site s where s.userId = ?1 and s.id = ?2")
    Site getSite(Long userId, Long id);

    /**
     * Updates the size to a site given a user id and a site id.
     * @param userId
     * @param siteId
     * @param size
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Site s set s.size = ?3 where s.userId = ?1 and s.id = ?2")
    void updateSiteSize(Long userId, Long id, int size);
    
    /**
     * Update the url and description of a given site.
     * @param userId
     * @param url
     * @param description
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Site s set s.url = ?3, s.description = ?4 where s.userId = ?1 and s.id = ?2")
    void updateSiteInfo(Long userId, Long id, String url, String description);
}
