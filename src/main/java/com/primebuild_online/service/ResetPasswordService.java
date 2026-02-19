package com.primebuild_online.service;

import com.primebuild_online.model.DTO.ResetPasswordDTO;
import com.primebuild_online.repository.UserRepository;

public interface ResetPasswordService {
    void createAndSendPin(String email);
    boolean resetPassword(ResetPasswordDTO resetPasswordDTO);
}
