package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nathaliebize.sphynx.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{
    @Query("select s from Session s where s.userId = ?1 and s.siteId= ?2")
    ArrayList<Session> getSessionList(Long userId, Long siteId);

}
