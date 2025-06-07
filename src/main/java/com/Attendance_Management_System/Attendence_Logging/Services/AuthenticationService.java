package com.Attendance_Management_System.Attendence_Logging.Services;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Attendance_Management_System.Attendence_Logging.DTOs.AuthenticationResponse;
import com.Attendance_Management_System.Attendence_Logging.Models.Role;
import com.Attendance_Management_System.Attendence_Logging.Models.StaffProfile;
import com.Attendance_Management_System.Attendence_Logging.Models.User;
import com.Attendance_Management_System.Attendence_Logging.Repositories.StaffProfileRepository;
import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StaffProfileRepository staffProfileRepository;

    public AuthenticationService(
        AuthenticationManager authenticationManager,
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        StaffProfileRepository staffProfileRepository
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.staffProfileRepository = staffProfileRepository;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setGoogleId(request.getGoogleId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(request.getCreatedAt());
        user.setRole(request.getRole() == null ? Role.STAFF : request.getRole());

        user = repository.save(user);
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .user(user)
            .setupComplete(false)
            .build();
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        boolean setupComplete = staffProfileRepository.findByUserId(user.getId())
            .map(StaffProfile::isSetupComplete)
            .orElse(false);

        return AuthenticationResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .user(user)
            .setupComplete(setupComplete)
            .build();
    }

    public AuthenticationResponse registerEmployeeWithNotification(User request) {
        User user = new User();

        String rawPassword = generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);

        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role must be provided");
        }

        user.setRole(request.getRole());
        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .user(user)
            .setupComplete(false)
            .build();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    public AuthenticationResponse googleLogin(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList("223155042733-ikov457vnkj7rucmhvc9di4j4ndvsvbg.apps.googleusercontent.com"))
            .build();

        GoogleIdToken token;
        try {
            token = verifier.verify(idToken);
        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed", e);
        }

        if (token == null) {
            throw new RuntimeException("Invalid Google ID token");
        }

        Payload payload = token.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String googleId = payload.getSubject();

        Optional<User> userOpt = repository.findByEmail(email);
        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = User.builder()
                .email(email)
                .username(name)
                .googleId(googleId)
                .role(Role.STAFF)
                .build();

            user = repository.save(user);
        }

        String jwt = jwtService.generateToken(user);

        // Check if the user has a StaffProfile and if setup is complete
        boolean setupComplete = staffProfileRepository.findByUserId(user.getId())
            .map(StaffProfile::isSetupComplete)
            .orElse(false);

        return AuthenticationResponse.builder()
            .token(jwt)
            .role(user.getRole().name())
            .user(user)
            .setupComplete(setupComplete)
            .build();
    }

}
