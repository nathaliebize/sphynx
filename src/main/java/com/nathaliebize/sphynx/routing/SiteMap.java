package com.nathaliebize.sphynx.routing;

/**
 * Enumeration of the different paths.
 */
public enum SiteMap {
    CSS_SPHYNX("/css/sphynx.css"),
    ERROR("/error"),
    ERROR_LOGOUT("/error-logout"),
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
    SCRIPT("/js/sphynx"),
    SESSIONS("/sessions"),
    SESSIONS_DELETE_REQUEST_CONFIRMATION("/sessions/delete-request-confirmation"),
    SESSIONS_INDEX("/sessions/sessions-list"),
    SESSIONS_TIMELINE("/sessions/timeline"),
    SITES("/sites/"),
    SITES_CREATE("/sites/create"),
    SITES_CREATE_CONFIRMATION("/sites/create-confirmation"),
    SITES_DELETE_REQUEST_CONFIRMATION("/sites/delete-request-confirmation"),
    SITES_EDIT("/sites/edit"),
    SITES_INDEX("/sites/sites-list"),
    TERMS("/terms"),
    USER_RESET_PASSWORD_REQUEST("/user/reset-password-request"),
    USER_LOGIN("/user/login"),
    USER_REGISTER("/user/register"),
    USER_RESET_PASSWORD("/user/reset-password"),
    USER_VERIFY("/user/verify"),
    USER_RESET_PASSWORD_CONFIRMATION("/user/reset-password-confirmation");
    
    private String path;
    
    private SiteMap(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
