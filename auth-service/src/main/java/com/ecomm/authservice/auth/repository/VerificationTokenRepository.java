package com.ecomm.authservice.auth.repository;

import com.ecomm.authservice.auth.entity.TokenType;
import com.ecomm.authservice.auth.entity.User;
import com.ecomm.authservice.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByUserAndType(User user, TokenType type);

    void deleteByUserAndType(User user, TokenType type);

    Optional<VerificationToken> findByCodeAndType(String code, TokenType type);
}