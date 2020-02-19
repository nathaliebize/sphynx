package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.Session;

/**
 * JPA Repository that connects to the sessions table.
 */
public interface SessionRepository extends JpaRepository<Session, Long>{
    /**
     * Gets a list of sessions for a given site.
     * @param userId
     * @param siteId
     * @return list of sessions
     */
    @Query("select s from Session s where s.userId = ?1 and s.siteId= ?2 order by s.startTime desc")
    ArrayList<Session> getSessionList(Long userId, Long siteId);
    
    /**
     * Updates the status of a given session.
     * @param status
     * @param sessionId
     */
    @Modifying
    @Transactional
    @Query("update Session s set s.status = ?1 where s.sessionId = ?2")
    void updateSessionStatus(String status, Long sessionId);

    /**
     * Deletes a session from table.
     * @param sessionId
     */
    @Modifying
    @Transactional
    @Query("delete Session s where s.sessionId = ?1")
    void deleteBySessionId(String sessionId);
    
    /**
     * Retrieves a session for a given session id.
     * @param sessionId
     * @return session
     */
    @Query("select s from Session s where s.sessionId = ?1")
    Session findBySessionId(String sessionId);

}
