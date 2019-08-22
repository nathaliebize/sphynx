package com.nathaliebize.sphynx.controller;

public enum SiteMap {
    ERROR("/error"),
    HOME("/"),
    INDEX("/index"),
    REDIRECT_ERROR("redirect:/error"),
    REDIRECT_HOME("redirect:/"),
    REDIRECT_SITES("redirect:/sites/"),
    SESSIONS("/sessions"),
    SITES("/sites"),
    TERMS("/terms"),
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
