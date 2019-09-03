package com.nathaliebize.sphynx.controller;

public enum SiteMap {
    CSS_SPHYNX("/css/sphynx.css"),
    HOME("/"),
    LOGOUT("/logout"),
    REDIRECT_ERROR("redirect:/error"),
    REDIRECT_HOME("redirect:/"),
    REDIRECT_SITES("redirect:/sites"),
    REDIRECT_USER_LOGIN("redirect:/user/login"),
    SESSIONS("/sessions"),
    SITES("/sites"),
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
