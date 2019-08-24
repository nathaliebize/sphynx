package com.nathaliebize.sphynx.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationGroupRepository extends JpaRepository<AuthorizationGroup, Long>{
    AuthorizationGroup findByEmail(String email);
}
