package com.Attendance_Management_System.Attendence_Logging.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attendance_Management_System.Attendence_Logging.Models.BiometricCredential;

public interface BiometricCredentialRepository extends JpaRepository<BiometricCredential, Long> {
    Optional<BiometricCredential> findByCredentialId(String credentialId);
    List<BiometricCredential> findByUserEmail(String email);
    Optional<BiometricCredential> findFirstByUserEmail(String email);
    Optional<BiometricCredential> findFirstByUserId(Long userId);
    
    
    
    List<BiometricCredential> findAllByCredentialId(String credentialId);
    Optional<BiometricCredential> findByCredentialIdAndUserId(String credentialId, Long userId);
}