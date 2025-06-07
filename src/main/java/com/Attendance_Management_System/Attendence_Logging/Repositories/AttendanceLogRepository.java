package com.Attendance_Management_System.Attendence_Logging.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attendance_Management_System.Attendence_Logging.Models.AttendanceLog;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    Optional<AttendanceLog> findByUserIdAndDate(Long userId, LocalDate date);
    List<AttendanceLog> findByUserId(Long userId);
}

