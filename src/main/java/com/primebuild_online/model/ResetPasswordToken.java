package com.primebuild_online.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token; // the PIN
    private LocalDateTime expiryDate;

    public ResetPasswordToken(LocalDateTime expiryDate, String email, String token) {
        this.expiryDate = expiryDate;
        this.token = token;
        this.email = email;
    }
}