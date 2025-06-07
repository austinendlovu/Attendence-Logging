package com.Attendance_Management_System.Attendence_Logging.Controllers;

import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Attendance_Management_System.Attendence_Logging.Models.AttendanceLog;
import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;
import com.Attendance_Management_System.Attendence_Logging.Services.AttendanceService;
import com.Attendance_Management_System.Attendence_Logging.Services.JwtService;
import com.google.common.base.FinalizablePhantomReference;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AttendanceController(AttendanceService attendanceService, JwtService jwtService,UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.jwtService = jwtService;
		this.userRepository = userRepository;
    }
    

    @PostMapping("/checkin/face")
    public ResponseEntity<?> checkInFace(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        AttendanceLog log = attendanceService.checkInWithFace(userId);
        return ResponseEntity.ok(log);
    }

    @PostMapping("/checkout/face")
    public ResponseEntity<?> checkOutFace(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        AttendanceLog log = attendanceService.checkOutWithFace(userId);
        return ResponseEntity.ok(log);
    }

    @PostMapping("/checkin/qr")
    public ResponseEntity<?> checkInQr(@RequestParam("userId") Long userId) {
        AttendanceLog log = attendanceService.checkInWithQr(userId);
        return ResponseEntity.ok(log);
    }

    @PostMapping("/checkout/qr")
    public ResponseEntity<?> checkOutQr(@RequestParam("userId") Long userId) {
        AttendanceLog log = attendanceService.checkOutWithQr(userId);
        return ResponseEntity.ok(log);
    }

    // ADMIN: Get all attendance records
    @GetMapping("/admin-logs")
    public ResponseEntity<?> getAllLogs() {
        List<AttendanceLog> logs = attendanceService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    // STAFF: Get my own attendance records
    @GetMapping("/my-logs")
    public ResponseEntity<?> getMyLogs(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        List<AttendanceLog> logs = attendanceService.getLogsForUser(userId);
        return ResponseEntity.ok(logs);
    }
}

