package com.Attendance_Management_System.Attendence_Logging.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String to, String resetLink) {
        String subject = "Reset Your Password - Attendance System";
        String htmlContent = buildResetEmailTemplate(resetLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            helper.setFrom("mukomiaustine8@gmail.com", "Attendance System");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Enable HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email to " + to, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during email sending", e);
        }
    }

    private String buildResetEmailTemplate(String resetLink) {
        return """
            <div style="font-family:Arial,sans-serif; padding:20px; background:#f9f9f9; color:#333;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; overflow:hidden; box-shadow:0 0 10px rgba(0,0,0,0.1);">
                    <div style="padding:20px; text-align:center; background:#4A90E2; color:#fff;">
                        <h2>Attendance System</h2>
                        <p>Password Reset Request</p>
                    </div>
                    <div style="padding:20px;">
                        <p>Hello,</p>
                        <p>We received a request to reset your password. Click the button below to set a new one:</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background:#4A90E2; color:#fff; padding:12px 25px; border-radius:5px; text-decoration:none;">Reset Password</a>
                        </div>
                        <p>If you didn’t request this, you can safely ignore this email.</p>
                        <p style="margin-top:40px;">Thank you,<br>The Attendance System Team</p>
                    </div>
                </div>
            </div>
        """.formatted(resetLink);
    }
    public void sendQrCodeEmail(String to, String qrUrl) {
        String subject = "Your QR Code - Attendance System";
        String htmlContent = buildQrCodeEmailTemplate(qrUrl);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"
            );

            helper.setFrom("mukomiaustine8@gmail.com", "Attendance System");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Enable HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send QR code email to " + to, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during QR email sending", e);
        }
    }

    private String buildQrCodeEmailTemplate(String qrUrl) {
        return """
            <div style="font-family:Arial,sans-serif; padding:20px; background:#f9f9f9; color:#333;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; overflow:hidden; box-shadow:0 0 10px rgba(0,0,0,0.1);">
                    <div style="padding:20px; text-align:center; background:#4A90E2; color:#fff;">
                        <h2>Attendance System</h2>
                        <p>Your QR Code for Attendance</p>
                    </div>
                    <div style="padding:20px; text-align:center;">
                        <p>Hello,</p>
                        <p>Please scan the QR code below to check in or check out daily:</p>
                        <img src="%s" alt="QR Code" style="width:200px; height:200px; margin:20px auto;" />
                        <p>Keep this email for future use. You can scan this QR code at the kiosk or from your phone.</p>
                        <p style="margin-top:40px;">Thank you,<br>The Attendance System Team</p>
                    </div>
                </div>
            </div>
        """.formatted(qrUrl);
    }

}
