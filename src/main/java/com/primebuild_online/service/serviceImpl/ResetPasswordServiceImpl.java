package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ResetPasswordDTO;
import com.primebuild_online.model.ResetPasswordToken;
import com.primebuild_online.repository.ResetPasswordTokenRepository;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.security.ResetPassword.EmailService;
import com.primebuild_online.security.ResetPassword.PinGenerator;
import com.primebuild_online.service.ResetPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final ResetPasswordTokenRepository tokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordServiceImpl(ResetPasswordTokenRepository tokenRepository,
                                    EmailService emailService,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createAndSendPin(String email) {
        String pin = PinGenerator.generatePin(6);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        tokenRepository.deleteByEmail(email);
        tokenRepository.save(new ResetPasswordToken(expiry, email, pin));

        emailService.sendResetPin(email, pin);
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Optional<ResetPasswordToken> optionalToken =
                tokenRepository.findByEmailAndToken(resetPasswordDTO.getEmail(), resetPasswordDTO.getPin());

        if (optionalToken.isEmpty()) {
            return false;
        }

        ResetPasswordToken token = optionalToken.get();

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        userRepository.findByEmail(resetPasswordDTO.getEmail()).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(user);
        });

        tokenRepository.delete(token);

        return true;
    }

}
