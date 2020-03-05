package com.nathaliebize.sphynx.model.view;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nathaliebize.sphynx.model.EventType;

/**
 * One event recorded by a sphynx-powered website.
 * sessionId, siteId, userId, type, date and path cannot be null.
 * Target property is optional.
 */
public class RecordedEvent {
    @NotNull
    private String sessionId;
    
    @NotNull
    private Long siteId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private EventType type;
        
    @NotNull
    private Date date;
    
    private String target;
    
    @NotNull
    private String path;

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

    public EventType getType() {
        return this.type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
