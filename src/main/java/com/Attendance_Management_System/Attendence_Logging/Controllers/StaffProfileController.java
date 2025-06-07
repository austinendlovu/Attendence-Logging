package com.Attendance_Management_System.Attendence_Logging.Controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Attendance_Management_System.Attendence_Logging.DTOs.StaffProfileSetupRequest;
import com.Attendance_Management_System.Attendence_Logging.Models.StaffProfile;
import com.Attendance_Management_System.Attendence_Logging.Services.JwtService;
import com.Attendance_Management_System.Attendence_Logging.Services.StaffProfileService;

@RestController
@RequestMapping("/api/profile")
public class StaffProfileController {

    private final StaffProfileService profileService;
    private final JwtService jwtService;

    public StaffProfileController(StaffProfileService profileService, JwtService jwtService) {
        this.profileService = profileService;
        this.jwtService = jwtService;
    }

    @PostMapping("/setup")
    public ResponseEntity<?> setupProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody StaffProfileSetupRequest request
    ) {
        Long userId = jwtService.extractUserId(token.replace("Bearer ", "").trim());
        StaffProfile profile = profileService.setupProfile(userId, request);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/upload-face")
    public ResponseEntity<?> uploadFace(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        Long userId = jwtService.extractUserId(token.replace("Bearer ", "").trim());
        StaffProfile profile = profileService.uploadFace(userId, file);
        return ResponseEntity.ok(profile);
    }
}
