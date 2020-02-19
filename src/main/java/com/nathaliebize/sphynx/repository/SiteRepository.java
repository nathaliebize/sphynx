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
     * Retrieves a site given a site id
     * @param id
     * @return site
     */
    @Query("select s from Site s where s.id = ?1")
    Site findBySiteId(Long id);

    /**
     * Updates the size to a given site.
     * @param id
     * @param size
     */
    @Modifying
    @Transactional
    @Query("update Site s set s.size = ?2 where s.id = ?1")
    void updateSiteSize(Long id, int size);
    
    /**
     * Update the url and description of a given site.
     * @param id
     * @param url
     * @param description
     */
    @Modifying
    @Transactional
    @Query("update Site s set s.url = ?2, s.description = ?3 where s.id = ?1")
    void updateSiteInfo(Long id, String url, String description);

}
