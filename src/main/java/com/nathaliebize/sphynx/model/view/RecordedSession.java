package com.nathaliebize.sphynx.model.view;

import javax.validation.constraints.NotNull;

public class RecordedSession {
    @NotNull
    private Long sessionId;
    
    @NotNull
    private Long siteId;
    
    @NotNull
    private Long userId;

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

}
