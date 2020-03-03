package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nathaliebize.sphynx.model.Session;

/**
 * JPA Repository that connects to the sessions table.
 */
public interface SessionRepository extends JpaRepository<Session, String>{
    /**
     * Gets a list of sessions for a given site.
     * @param userId
     * @param siteId
     * @return list of sessions
     */
    @Query("select s from Session s where s.userId = ?1 and s.siteId= ?2 order by s.date desc")
    ArrayList<Session> getSessionList(Long userId, Long siteId);
    
    /**
     * Retrieves a session for a given session id.
     * @param userId
     * @param sessionId
     * @return session
     */
    @Query("select s from Session s where s.userId = ?1 and s.id = ?2")
    Session getSession(Long userId, String sessionId);
}
