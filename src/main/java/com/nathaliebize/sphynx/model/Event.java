package com.nathaliebize.sphynx.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Event represents one event that has been triggers powered by sphynx app.
 * It is linked to the sites table in the database.
 */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(generator = "events_generator")
    @SequenceGenerator(
            name = "events_generator",
            sequenceName = "events_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @NotNull
    private String sessionId;
    
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
    
    @NotNull
    private String path;

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
    
    /**
     * Calculates the duration between a given start and the timestamp of this event.
     * @param start time (precedent event timestamp)
     * @return (HH:)MM:SS
     */
    public String getDuration(Date startTime) {
        long elapsedTime = this.timestamp.getTime() - startTime.getTime();
        
        int hours = Math.toIntExact(elapsedTime / (60 * 60 * 1000) % 24);
        int minutes = Math.toIntExact(elapsedTime / (60 * 1000) % 60);
        int seconds = Math.toIntExact(elapsedTime / 1000 % 60);
        int roundedMinutes = minutes + (seconds > 30 ? 1 : 0);
        
        if (hours > 0) {
            return String.valueOf(hours) + "h" + String.valueOf(roundedMinutes);
        } else if (minutes > 0) {
            return String.valueOf(roundedMinutes) + " min";
        } else if (seconds > 0) {
            return String.valueOf(seconds) + " sec";
        } else {
            return "";
        }         
    }
    
}
