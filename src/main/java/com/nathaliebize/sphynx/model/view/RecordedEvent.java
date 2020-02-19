package com.nathaliebize.sphynx.model.view;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nathaliebize.sphynx.model.EventType;

public class RecordedEvent {
    @NotNull
    private Long sessionId;
    
    @NotNull
    private Long siteId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private EventType type;
    
    private int groupType;
    
    @NotNull
    private Date timestamp;
    
    private String target;
    
    private String path;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
