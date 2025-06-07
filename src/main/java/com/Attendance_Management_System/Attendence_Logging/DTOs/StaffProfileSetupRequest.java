package com.Attendance_Management_System.Attendence_Logging.DTOs;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfileSetupRequest {

    private LocalTime startTime;
    private LocalTime endTime;
}
