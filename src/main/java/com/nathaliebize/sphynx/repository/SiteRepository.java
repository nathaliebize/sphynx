package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long>{
    @Query("select s from Site s where s.userId = ?1")
    ArrayList<Site> findByUserId(Long userId);
    
    @Query("select s from Site s where s.id = ?1")
    Site findBySiteId(Long id);

    @Modifying
    @Transactional
    @Query("update Site s set s.size = ?2 where s.id = ?1")
    void updateSiteSize(Long id, int size);

}
