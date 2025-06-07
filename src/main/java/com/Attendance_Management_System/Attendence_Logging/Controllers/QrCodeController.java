package com.Attendance_Management_System.Attendence_Logging.Controllers;


import com.Attendance_Management_System.Attendence_Logging.Models.QrCodeData;
import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;
import com.Attendance_Management_System.Attendence_Logging.Services.JwtService;
import com.Attendance_Management_System.Attendence_Logging.Services.QrCodeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class QrCodeController {

    private final JwtService jwtService;
    private final QrCodeService qrCodeService;
    private final UserRepository userRepo;

    public QrCodeController(JwtService jwtService, QrCodeService qrCodeService, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.qrCodeService = qrCodeService;
        this.userRepo = userRepo;
    }

    @PostMapping("/qrcode")
    public ResponseEntity<?> generateQr(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        String email = userRepo.findById(userId).orElseThrow().getEmail();

        try {
            QrCodeData qr = qrCodeService.generateAndSendQr(userId, email);
            return ResponseEntity.ok(qr);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("QR Code generation failed");
        }
    }
}
