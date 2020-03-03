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
    private Date date;
    
    @NotNull
    private String host;
    
    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
