package com.nathaliebize.sphynx.model.view;

import javax.validation.constraints.NotBlank;

import com.nathaliebize.sphynx.security.constraint.UrlField;

/**
 * One site that is submited by user to be saved in database.
 * The url and description properties cannot be null.
 */
public class SubmitedSite {
    @NotBlank(message="Must not be blank")
    @UrlField(message="Must be a valid url")
    private String url;
    
    @NotBlank(message="Must not be blank")
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
