package com.Attendance_Management_System.Attendence_Logging.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Attendance_Management_System.Attendence_Logging.Models.AttendanceLog;
import com.Attendance_Management_System.Attendence_Logging.Repositories.AttendanceLogRepository;

@Service
public class AttendanceService {

    private final AttendanceLogRepository logRepo;
    private final JwtService jwtService;

    public AttendanceService(AttendanceLogRepository logRepo, JwtService jwtService) {
        this.logRepo = logRepo;
        this.jwtService = jwtService;
    }

    public AttendanceLog checkInWithFace(Long userId) {
        LocalDate today = LocalDate.now();
        AttendanceLog log = logRepo.findByUserIdAndDate(userId, today)
                .orElse(new AttendanceLog());

        log.setUserId(userId);
        log.setDate(today);
        log.setCheckInTime(LocalDateTime.now());
        log.setMethod("FACE");

        return logRepo.save(log);
    }

    public AttendanceLog checkOutWithFace(Long userId) {
        LocalDate today = LocalDate.now();
        AttendanceLog log = logRepo.findByUserIdAndDate(userId, today)
                .orElseThrow(() -> new RuntimeException("No check-in record found"));

        log.setCheckOutTime(LocalDateTime.now());
        log.setMethod("FACE");

        return logRepo.save(log);
    }

    public AttendanceLog checkInWithQr(Long userId) {
        LocalDate today = LocalDate.now();
        AttendanceLog log = logRepo.findByUserIdAndDate(userId, today)
                .orElse(new AttendanceLog());

        log.setUserId(userId);
        log.setDate(today);
        log.setCheckInTime(LocalDateTime.now());
        log.setMethod("QR");

        return logRepo.save(log);
    }

    public AttendanceLog checkOutWithQr(Long userId) {
        LocalDate today = LocalDate.now();
        AttendanceLog log = logRepo.findByUserIdAndDate(userId, today)
                .orElseThrow(() -> new RuntimeException("No check-in record found"));

        log.setCheckOutTime(LocalDateTime.now());
        log.setMethod("QR");

        return logRepo.save(log);
    }
    public List<AttendanceLog> getAllLogs() {
        return logRepo.findAll();
    }

    public List<AttendanceLog> getLogsForUser(Long userId) {
        return logRepo.findByUserId(userId);
    }
}

