package com.nathaliebize.sphynx.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nathaliebize.sphynx.model.ViewEvent;

@Repository
public interface ViewEventRepository extends JpaRepository<ViewEvent, Long>{
    @Query("select v from ViewEvent v where v.userId = ?1 and v.sessionId = ?2")
    ArrayList<ViewEvent> getViewEventList(Long userId, Long sessionId);

}
