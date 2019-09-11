package com.nathaliebize.sphynx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sessions")
public class Session {    
    @Id
    @GeneratedValue(generator = "sessions_generator")
    @SequenceGenerator(
            name = "sessions_generator",
            sequenceName = "sessions_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @NotNull
    @Column(name = "session_id")
    private Long sessionId;
    
    @NotNull
    @Column(name = "site_id")
    private Long siteId;
    
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "session_status")
    private String status = "STARTED";

    public Long getSessionId() {
        return sessionId;
    }


    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }


    public Long getId() {
        return id;
    }


    public Long getSiteId() {
        return siteId;
    }


    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long user_id) {
        this.userId = user_id;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

}
