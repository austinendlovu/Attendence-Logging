package com.Attendance_Management_System.Attendence_Logging.Models;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QrCodeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String qrCodeUrl; // Cloudinary URL
}
