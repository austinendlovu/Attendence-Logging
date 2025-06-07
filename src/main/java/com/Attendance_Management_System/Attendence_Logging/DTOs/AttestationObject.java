package com.Attendance_Management_System.Attendence_Logging.DTOs;

import lombok.Data;

@Data
public class AttestationObject {
    private String id;
    private String type;  // e.g. "public-key"
    private AttestationResponse response;

    @Data
    public static class AttestationResponse {
        private String attestationObject; // base64url encoded string
        private String clientDataJSON;    // base64url encoded string
    }
}
