package com.nathaliebize.sphynx.model;

public enum EventType {
     
    CHANGE_URL("Opened new page"),
    CLICK("Clicked"),
    INACTIVE("Stopped interaction"),
    KEYDOWN("Typed"), 
    LEAVE_TAB("Left tab"),
    RETURN_TAB("Returned to tab"),
    START("Started session"),
    VIEW_ONE_HUNDRED("Reached end of the document");
    
    private String activity;
    
    private EventType(String activity) {
        this.activity = activity;
    }
    
    public String getActivity() {
        return activity;
    }
}
