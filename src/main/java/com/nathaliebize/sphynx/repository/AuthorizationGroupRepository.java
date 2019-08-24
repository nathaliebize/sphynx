package com.nathaliebize.sphynx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nathaliebize.sphynx.model.AuthorizationGroup;

@Repository
public interface AuthorizationGroupRepository extends JpaRepository<AuthorizationGroup, Long>{
    @Query("select a from AuthorizationGroup a where a.email = ?1")
    AuthorizationGroup findByEmail(String email);
}
