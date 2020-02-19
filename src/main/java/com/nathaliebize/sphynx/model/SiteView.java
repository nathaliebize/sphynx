package com.nathaliebize.sphynx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sites_view")
public class SiteView {
    @Id
    @GeneratedValue(generator = "sites_view_generator")
    @SequenceGenerator(
            name = "sites_view_generator",
            sequenceName = "sites_view_sequence",
            initialValue = 1000
    )
    private Long id;
    
    private String url;
    
    private String description;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "site_id")
    private Long siteId;
    
    @Column(name = "session_list_size")
    private int sessionListSize;
    
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public int getSessionListSize() {
//        int size = -1;
//        ArrayList<Session> sessionList = siteService.getSessionList(this.site.getUserId(), this.site.getId());
//        if (sessionList != null) {
//            size = sessionList.size();
//        }
        return sessionListSize;
    }
    
    public void setSessionListSize(int sessionListSize) {
        this.sessionListSize = sessionListSize;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }
    
}
