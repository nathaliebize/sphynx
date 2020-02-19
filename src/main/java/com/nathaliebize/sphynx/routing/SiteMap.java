package com.nathaliebize.sphynx.routing;

/**
 * Enumeration of the different paths.
 */
public enum SiteMap {
    CSS_SPHYNX("/css/sphynx.css"),
    EMAIL("#"),
    ERROR("/error"),
    GITHUB("https://github.com/nathaliebize/sphynx"),
    HOME("/"),
    INDEX("index"),
    INFO("/info"),
    LINKEDIN("https://www.linkedin.com/in/nathalie-bize-474ba5132"),
    LOGOUT("/logout"),
    REDIRECT_ERROR_LOGOUT("redirect:/error-logout"),
    REDIRECT_HOME("redirect:/"),
    REDIRECT_SESSIONS("redirect:/sessions/"),
    REDIRECT_SITES("redirect:/sites/"),
    REDIRECT_SITES_CREATE("redirect:/sites/create"),
    REDIRECT_USER_LOGIN("redirect:/user/login"),
    RESUME("#"),
    SCRIPT("/js/sphynx"),
    SESSIONS("/sessions"),
    SESSIONS_DELETE_CONFIRMATION("/sessions/delete-confirmation"),
    SESSIONS_INDEX("/sessions/sessions-list"),
    SESSIONS_TIMELINE("/sessions/timeline"),
    SITES("/sites/"),
    SITES_CREATE("/sites/create"),
    SITES_CREATE_CONFIRMATION("/sites/create-confirmation"),
    SITES_DELETE_CONFIRMATION("/sites/delete-confirmation"),
    SITES_EDIT("/sites/edit"),
    SITES_INDEX("/sites/sites-list"),
    TERMS("/terms"),
    URL_BASE("https://www.sphynx/"),
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
