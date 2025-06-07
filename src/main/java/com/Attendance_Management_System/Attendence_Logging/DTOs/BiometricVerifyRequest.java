package com.Attendance_Management_System.Attendence_Logging.DTOs;

import lombok.Data;

@Data
public class BiometricVerifyRequest {
    private String credentialId;
    private String providedPublicKey; // or a signed challenge depending on frontend implementation
}
