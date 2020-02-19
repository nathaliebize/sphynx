package com.nathaliebize.sphynx.repository;

// import java.util.Optional;
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
    
//    /**
//     * Retrieves an user given the email and password
//     * @param emailAddress
//     * @param password
//     * @return user
//     */
//    @Query("select u from User u where u.email = ?1 and u.password= ?2")
//    User findByEmailAndPassword(String emailAddress, String password);
//    
//    /**
//     * Retrieves an user given the email and registration key
//     * @param emailAdress
//     * @param key
//     * @return user
//     */
//    @Query("select u from User u where u.email = ?1 and u.registrationKey= ?2")
//    User findByEmailAndKey(String emailAdress, String key);
//    
//    /**
//     * Retrieves an user given the user id
//     * @param id
//     * @return user
//     */
//    @Query("select u from User u where u.id = ?1")
//    Optional<User> findById(Long id);
//    
//    /**
//     * Retrieves an user given the registration key
//     * @param key
//     * @return user
//     */
//    @Query("select u from User u where u.registrationKey = ?1")
//    User findByRegistrationKey(String key);
    
    /**
     * Updates the registration status for a given user.
     * @param status
     * @param email
     */
    @Modifying
    @Transactional
    @Query("update User u set u.registrationStatus = ?1 where u.email = ?2")
    void changeRegistrationStatus(String status, String email);
    
    /**
     * Updates the password for a given user.
     * @param password
     * @param key
     */
    @Modifying
    @Transactional
    @Query("update User u set u.password = ?1 where u.registrationKey = ?2")
    void updatePassword(String password, String key);
    
    /**
     * Updates the registration key for a given user.
     * @param key
     * @param email
     */
    @Modifying
    @Transactional
    @Query("update User u set u.registrationKey = ?1 where u.email = ?2")
    void updateRegistrationKey(String key, String email);
}
