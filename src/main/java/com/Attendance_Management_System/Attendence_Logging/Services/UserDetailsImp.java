package com.Attendance_Management_System.Attendence_Logging.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;



@Service
public class UserDetailsImp implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public UserDetailsImp(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("User not found"));
	}
      
	
}
