package com.Attendance_Management_System.Attendence_Logging.Services;

import com.Attendance_Management_System.Attendence_Logging.Exceptions.PasswordResetException;
import com.Attendance_Management_System.Attendence_Logging.Models.User;
import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void sendResetToken(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

            // Generate random token
            String token = UUID.randomUUID().toString();

            // Set token expiry (1 hour from now)
            LocalDateTime expiry = LocalDateTime.now().plusHours(1);

            // Save token and expiry to user
            user.setResetToken(token);
            user.setResetTokenExpiry(expiry);
            userRepository.save(user);

            // Create reset link with token
            String resetLink = "http://localhost:8081/reset-password?token=" + token;

            emailService.sendResetPasswordEmail(email, resetLink);

        } catch (UsernameNotFoundException e) {
            throw new PasswordResetException("User with email not found");
        } catch (Exception e) {
            throw new PasswordResetException("Failed to send reset email");
        }
    }

    public void resetPassword(String token, String newPassword) {
        try {
            User user = userRepository.findByResetToken(token)
                    .orElseThrow(() -> new PasswordResetException("Invalid or expired token"));

            // Check if token expired
            if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new PasswordResetException("Token expired");
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));

            // Clear token and expiry
            user.setResetToken(null);
            user.setResetTokenExpiry(null);

            userRepository.save(user);

        } catch (PasswordResetException e) {
            throw e;
        } catch (Exception e) {
            throw new PasswordResetException("Could not reset password");
        }
    }
}
