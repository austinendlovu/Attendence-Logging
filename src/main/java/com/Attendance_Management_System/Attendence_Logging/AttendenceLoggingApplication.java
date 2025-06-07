package com.Attendance_Management_System.Attendence_Logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AttendenceLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendenceLoggingApplication.class, args);
	}

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
