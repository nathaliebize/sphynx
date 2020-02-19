package com.nathaliebize.sphynx.controller;

public enum SiteMap {
    CSS_SPHYNX("/css/sphynx.css"),
    HOME("/"),
    INDEX("index"),
    LOGOUT("/logout"),
    REDIRECT_ERROR("redirect:/error"),
    REDIRECT_HOME("redirect:/"),
    REDIRECT_SESSIONS("redirect:/sessions/"),
    REDIRECT_SITES("redirect:/sites/"),
    REDIRECT_SITES_CREATE("redirect:/sites/create"),
    REDIRECT_USER_LOGIN("redirect:/user/login"),
    SESSIONS("/sessions/sessions-list"),
    SESSIONS_TIMELINE("/sessions/timeline"),
    SITES("/sites/"),
    SITES_CREATE("/sites/create"),
    SITES_CREATE_CONFIRMATION("/sites/create-confirmation"),
    SITES_DELETE("/sites/delete"),
    SITES_INDEX("/sites/sites-list"),
    TERMS("/terms"),
    URL_BASE("http://localhost:8080"),
    USER_FORGOT_PASSWORD("/user/forgot-password"),
    USER_LOGIN("/user/login"),
    USER_REGISTER("/user/register"),
    USER_RESET_PASSWORD("/user/reset-password"),
    USER_VERIFY("/user/verify"),
    VERIFY("/verify");
    
    private String path;
    
    private SiteMap(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
