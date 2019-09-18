package com.nathaliebize.sphynx.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "view_events")
public class ViewEvent {
    @Id
    @GeneratedValue(generator = "view_events_generator")
    @SequenceGenerator(
            name = "view_events_generator",
            sequenceName = "view_events_sequence",
            initialValue = 1000
    )
    private Long id;
    
    private Long sessionId;
    
    private Long siteId;
    
    private Long userId;
    
    private EventType type;
    
    private int groupType;
    
    private Date timeStart;
    
    private String target;
    
    private String path;
    
    private Date timeStop;
        
    public ViewEvent() {}
    
    public ViewEvent(Event event) {
        this.sessionId = event.getSessionId();
        this.siteId = event.getSiteId();
        this.userId = event.getUserId();
        this.type = event.getType();
        this.groupType = event.getGroupType();
        this.timeStart = event.getTimestamp();
        this.target = event.getTarget();
        this.path = event.getPath();
    }

    public Long getId() {
        return id;
    }

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

    
    public Date getTimeStart() {
        return timeStart;
    }


    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
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

    public Date getTimeStop() {
        return timeStop;
    }


    public void setTimeStop(Date timeStop) {
        this.timeStop = timeStop;
    }


    public Long getDuration() {
        if (timeStart != null && timeStop != null) {
            return (this.timeStop.getTime() - this.timeStart.getTime()) / 60;
        } else {
            return null;
        }
    }
}
