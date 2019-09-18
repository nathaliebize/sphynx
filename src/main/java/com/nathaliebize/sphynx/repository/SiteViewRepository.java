package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.SiteView;

public interface SiteViewRepository extends JpaRepository<SiteView, Long>{
    @Query("select sv from SiteView sv where sv.userId = ?1")
    ArrayList<SiteView> findByUserId(Long userId);

    @Query("select sv from SiteView sv where sv.siteId = ?1")
    SiteView findBySiteId(Long id);
    
    @Modifying
    @Transactional
    @Query("update SiteView sv set sv.sessionListSize = ?2 where sv.id = ?1")
    void updateSessionListSize(Long id, int size);

}
