package com.nathaliebize.sphynx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Session represents one session on one site powered by sphynx app.
 * It is linked to the sessions table in the database.
 */
@Entity
@Table(name = "sessions")
public class Session {     
    @Id
    @NotNull
    @Column(name = "id")
    private String id;
    
    @NotNull
    @Column(name = "site_id")
    private Long siteId;
    
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    
    @NotNull
    @Column(name = "date")
    private Date date;

    @NotNull
    @Column(name = "url")
    private String host;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String url) {
        this.host = url;
    }
}
