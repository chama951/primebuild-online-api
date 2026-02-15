package com.primebuild_online.model.DTO;

import com.primebuild_online.model.enumerations.SignUpMethods;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private LocalDateTime credentialsExpiryDate;
    private LocalDateTime accountExpiryDate;
    private boolean twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private SignUpMethods signUpMethod;
    private Long roleId;
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
