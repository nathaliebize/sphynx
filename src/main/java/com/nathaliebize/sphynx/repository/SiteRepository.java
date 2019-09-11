package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nathaliebize.sphynx.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long>{
    @Query("select s from Site s where s.user_id = ?1")
    ArrayList<Site> findByUserId(Long user_id);

}
