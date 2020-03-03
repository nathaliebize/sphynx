package com.nathaliebize.sphynx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.User;

/**
 * JPA Repository that connects to the users table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves an user given the email.
     * @param emailAddress
     * @return user
     */
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String emailAddress);
    
    /**
     * Updates the registration status for a given user.
     * @param status
     * @param email
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.registrationStatus = ?1 where u.email = ?2")
    void updateRegistrationStatus(String registrationStatus, String email);
    
    /**
     * Updates the password for a given user.
     * @param password
     * @param key
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.password = ?1 where u.registrationKey = ?2")
    void updatePassword(String password, String registrationKey);
    
    /**
     * Updates the registration key for a given user.
     * @param key
     * @param email
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.registrationKey = ?1 where u.email = ?2")
    void updateRegistrationKey(String registrationKey, String email);
}
