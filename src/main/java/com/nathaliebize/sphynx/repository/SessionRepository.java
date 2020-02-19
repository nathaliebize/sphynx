package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{
    @Query("select s from Session s where s.userId = ?1 and s.siteId= ?2")
    ArrayList<Session> getSessionList(Long userId, Long siteId);
    
    @Modifying
    @Transactional
    @Query("update Session s set s.status = ?1 where s.sessionId = ?2")
    void changeSessionStatus(String status, Long sessionId);

    @Modifying
    @Transactional
    @Query("delete Session s where s.sessionId = ?1")
    void deleteBySessionId(Long sessionId);
    
    @Query("select s from Session s where s.sessionId = ?1")
    Session findBySessionId(Long sessionId);

}
