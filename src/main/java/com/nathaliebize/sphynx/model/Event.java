package com.nathaliebize.sphynx.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Column;
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
    @Column(name = "id")
    @SequenceGenerator(
            name = "events_generator",
            sequenceName = "events_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @NotNull
    @Column(name = "session_id")
    private String sessionId;
    
    @NotNull
    @Column(name = "site_id")
    private Long siteId;
    
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    
    @NotNull
    @Column(name = "type")
    private EventType type;
    
    @NotNull
    @Column(name = "date")
    private Date date;
    
    @Column(name = "target")
    private String target;
    
    @NotNull
    @Column(name = "path")
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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
     * Calculates the duration between a given start and the date of this event.
     * @param Date: start time (precedent event date)
     * @return String: HhMM or MM min or SS sec.
     */
    public String getDuration(Date startDate) {
        LocalDateTime end = this.date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime start = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if ((start.isAfter(end))) {
            return "###";
        }
        long days = start.until(end, ChronoUnit.DAYS);
        if (days >= 1) {
            return "24h+";
        }
        LocalDateTime tempDateTime = LocalDateTime.from(start);
        long hours = start.until(end, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);
        long minutes = tempDateTime.until(end, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);
        long seconds = tempDateTime.until(end, ChronoUnit.SECONDS);
        minutes += (seconds > 30 ? 1 : 0);
        if (hours > 0) {
            return String.valueOf(hours) + "h" + String.valueOf((minutes > 9 ? "" : "0") + minutes);
        } else if (minutes > 0) {
            return String.valueOf(minutes + " min");
        } else if (seconds > 0) {
            return String.valueOf(seconds + " sec");
        } else {
            return "";
        }         
    }
    
}
