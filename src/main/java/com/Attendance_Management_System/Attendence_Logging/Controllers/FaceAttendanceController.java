package com.Attendance_Management_System.Attendence_Logging.Controllers;

import com.Attendance_Management_System.Attendence_Logging.DTOs.FaceRequest;
import com.Attendance_Management_System.Attendence_Logging.Models.AttendanceLog;
import com.Attendance_Management_System.Attendence_Logging.Services.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/face")
public class FaceAttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestBody FaceRequest request) {
        if (!verifyFaceWithPython(request.getUserId(), request.getFaceImage())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Face mismatch");
        }

        AttendanceLog log = attendanceService.checkInWithFace(request.getUserId());
        return ResponseEntity.ok(log);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestBody FaceRequest request) {
        if (!verifyFaceWithPython(request.getUserId(), request.getFaceImage())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Face mismatch");
        }

        AttendanceLog log = attendanceService.checkOutWithFace(request.getUserId());
        return ResponseEntity.ok(log);
    }

    private boolean verifyFaceWithPython(Long userId, String base64Image) {
        String url = "http://localhost:5000/verify-face";

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId.toString());
        payload.put("faceImage", base64Image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, 
                (Class<Map<String, Object>>) (Class<?>) Map.class);

            Map<String, Object> body = response.getBody();

            if (body != null && Boolean.TRUE.equals(body.get("match"))) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error verifying face: " + e.getMessage());
        }

        return false;
    }

}
