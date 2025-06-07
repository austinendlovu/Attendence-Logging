package com.Attendance_Management_System.Attendence_Logging.DTOs;

import lombok.Data;

@Data
public class BiometricRegisterRequest {
    private Long userId;
    private String id; // Credential ID from client
    private String attestationObject; // Base64URL string from client
    private String clientDataJSON;    // Base64URL string from client
}
