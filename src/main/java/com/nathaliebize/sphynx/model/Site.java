package com.nathaliebize.sphynx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Site represents one site powered by sphynx app.
 * It is linked to the sites table in the database.
 */
@Entity
@Table(name = "sites")
public class Site {
    
    @Id
    @GeneratedValue(generator = "sites_generator")
    @SequenceGenerator(
            name = "sites_generator",
            sequenceName = "sites_sequence",
            initialValue = 1000
    )
    private Long id;
    
    @NotNull
    private String url;
    
    @NotNull
    private String description;
    
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    
    private int size = 0;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
}
