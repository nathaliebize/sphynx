package com.nathaliebize.sphynx.model.view;

import javax.validation.constraints.NotNull;

/**
 * CreatedSite represents one site sudmited by user.
 */
public class CreatedSite {

    
    @NotNull
    private String url;
    
    @NotNull
    private String description;

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
}
