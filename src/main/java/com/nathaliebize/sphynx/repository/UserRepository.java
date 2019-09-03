package com.nathaliebize.sphynx.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nathaliebize.sphynx.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String emailAddress);
    
    @Query("select u from User u where u.email = ?1 and u.password= ?2")
    User findByEmailAndPassword(String emailAddress, String password);
    
    @Query("select u from User u where u.email = ?1 and u.registrationKey= ?2")
    User findByEmailAndKey(String emailAdress, String key);
    
    @Query("select u from User u where u.id = ?1")
    Optional<User> findById(Long id);
    
    @Query("select u from User u where u.registrationKey = ?1")
    User findByRegistrationKey(String key);
    
    @Modifying
    @Transactional
    @Query("update User u set u.registrationStatus = ?1 where u.email = ?2")
    void changeRegistrationStatus(String status, String email);
    
    @Modifying
    @Transactional
    @Query("update User u set u.password = ?1 where u.registrationKey = ?2")
    void updatePassword(String password, String key);
    
    @Modifying
    @Transactional
    @Query("update User u set u.registrationKey = ?1 where u.email = ?2")
    void updateRegistrationKey(String key, String email);
}
