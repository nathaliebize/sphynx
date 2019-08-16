package com.nathaliebize.sphynx.repository;

import com.nathaliebize.sphynx.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String emailAddress);
    
    @Modifying
    @Transactional
    @Query("update User u set u.status = ?1 where u.email = ?2")
    void changeStatus(String status, String email);
    
    @Query("select u from User u where u.email = ?1 and u.password= ?2")
    User findByEmailAndPassword(String emailAddress, String password);
}
