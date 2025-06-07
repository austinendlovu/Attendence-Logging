package com.Attendance_Management_System.Attendence_Logging.DTOs;

import lombok.Data;

@Data
public class FaceRequest {
    private Long userId;
    private String faceImage; // base64 string
}

