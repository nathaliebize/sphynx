package com.nathaliebize.sphynx.model.view;

import javax.validation.constraints.NotNull;

/**
 * One site that is submited by user to be saved in database.
 * The url and description properties cannot be null.
 */
public class SubmitedSite {
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
