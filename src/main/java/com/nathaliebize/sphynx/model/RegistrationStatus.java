package com.nathaliebize.sphynx.model;

public enum RegistrationStatus {
    VERIFIED("verified"), UNVERIFIED("unverified");
    
    private String status;
    
    public String getString() {
        return this.status;
    }
    
    private RegistrationStatus(String status) {
        this.status = status;
    }
}
