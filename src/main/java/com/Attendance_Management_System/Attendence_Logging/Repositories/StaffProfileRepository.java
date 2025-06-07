package com.Attendance_Management_System.Attendence_Logging.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attendance_Management_System.Attendence_Logging.Models.StaffProfile;


public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
    Optional<StaffProfile> findByUserId(Long userId);
}
