package com.ecomm.authservice.auth.repository;

import com.ecomm.authservice.auth.entity.AppRole;
import com.ecomm.authservice.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole roleName);
}