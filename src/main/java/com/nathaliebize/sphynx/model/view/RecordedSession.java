package com.nathaliebize.sphynx.model.view;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * One session recorded by a sphynx-powered website.
 * sessionId, siteId, userId and startTime cannot be null.
 */
public class RecordedSession {
    @NotNull
    private String sessionId;
    
    @NotNull
    private Long siteId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Date startTime;
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
