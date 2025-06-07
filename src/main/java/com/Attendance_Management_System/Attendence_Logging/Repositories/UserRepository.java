package com.Attendance_Management_System.Attendence_Logging.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attendance_Management_System.Attendence_Logging.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	 Optional<User> findByUsername(String username);

	    boolean existsByUsername(String username);
	    Optional<User> findByEmail(String email);
	    
	    Optional<User> findByResetToken(String resetToken);
	    
}
