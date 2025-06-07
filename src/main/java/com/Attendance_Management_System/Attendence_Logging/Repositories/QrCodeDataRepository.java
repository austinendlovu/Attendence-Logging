package com.Attendance_Management_System.Attendence_Logging.Repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Attendance_Management_System.Attendence_Logging.Models.QrCodeData;

public interface QrCodeDataRepository extends JpaRepository<QrCodeData, Long> {
    Optional<QrCodeData> findByUserId(Long userId);
}
