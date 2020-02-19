package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nathaliebize.sphynx.model.Event;

/**
 * JPA Repository that connects to the events table.
 */
public interface EventRepository extends JpaRepository <Event, Long> {
    /**
     * Gets the list of events for a given session.
     * @param userId
     * @param sessionId
     * @return list of events
     */
    @Query("select e from Event e where e.userId = ?1 and e.sessionId = ?2 order by e.timestamp asc")
    ArrayList<Event> getEventList(Long userId, String sessionId);
}
