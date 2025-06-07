package com.Attendance_Management_System.Attendence_Logging.DTOs;

import com.Attendance_Management_System.Attendence_Logging.Models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;
    private String role;
    private User user;
    private boolean setupComplete;
}
