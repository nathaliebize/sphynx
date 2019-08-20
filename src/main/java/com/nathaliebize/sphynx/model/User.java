package com.nathaliebize.sphynx.model;

public class User {
        private Long id;
        
        private String email;
        
        private String password;
        
        private String registrationKey;
        
        private RegistrationStatus registrationStatus = RegistrationStatus.UNVERIFIED;
        
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        
        public RegistrationStatus getRegistrationStatus() {
            return registrationStatus;
        }

        public void setRegistrationStatus(RegistrationStatus registrationStatus) {
            this.registrationStatus = registrationStatus;
        }
        
        public String getRegistrationKey() {
            return registrationKey;
        }
        
        public void setRegistrationKey(String registrationKey) {
            this.registrationKey = registrationKey;
        }

}
