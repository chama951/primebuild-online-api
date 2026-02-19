package com.primebuild_online.repository;

import com.primebuild_online.model.ResetPasswordToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    Optional<ResetPasswordToken> findByEmailAndToken(String email, String token);

    @Transactional
    void deleteByEmail(String email);
}