package com.Attendance_Management_System.Attendence_Logging.Controllers;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Attendance_Management_System.Attendence_Logging.DTOs.BiometricOptionRequest;
import com.Attendance_Management_System.Attendence_Logging.DTOs.BiometricRegisterRequest;
import com.Attendance_Management_System.Attendence_Logging.DTOs.BiometricVerifyRequest;
import com.Attendance_Management_System.Attendence_Logging.Models.BiometricCredential;
import com.Attendance_Management_System.Attendence_Logging.Models.User;
import com.Attendance_Management_System.Attendence_Logging.Repositories.BiometricCredentialRepository;
import com.Attendance_Management_System.Attendence_Logging.Repositories.UserRepository;
import com.Attendance_Management_System.Attendence_Logging.Services.QrCodeService;

import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.AuthenticatorAttestationResponse;
import com.webauthn4j.util.Base64UrlUtil;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/biometrics")
@RequiredArgsConstructor
public class BiometricController {

    private final BiometricCredentialRepository biometricRepo;
    private final UserRepository userRepository;
    private final QrCodeService grCodeService;

    

    @PostMapping("/registration/options")
    public ResponseEntity<?> getRegistrationOptions(@RequestBody BiometricOptionRequest request) {
        Long userId = request.getUserId();

        if (userId == null) {
            return ResponseEntity.badRequest().body("Missing userId");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();

        Map<String, Object> options = new HashMap<>();
        options.put("rp", Map.of("name", "Attendance Management System"));
        options.put("user", Map.of(
            "id", Base64.getUrlEncoder().withoutPadding().encodeToString(user.getId().toString().getBytes()),
            "name", user.getEmail(),
            "displayName", user.getEmail()
        ));
        options.put("challenge", grCodeService.generateChallenge());
        options.put("pubKeyCredParams", List.of(
            Map.of("type", "public-key", "alg", -7),
            Map.of("type", "public-key", "alg", -257)
        ));
        options.put("authenticatorSelection", Map.of(
            "authenticatorAttachment", "platform",
            "userVerification", "required"
        ));
        options.put("timeout", 60000);
        options.put("attestation", "direct");

        return ResponseEntity.ok(options);
    }

    /*@PostMapping("/register")
    public ResponseEntity<?> registerBiometric(@RequestBody BiometricRegisterRequest request) {
        try {
            User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

            byte[] attestationObjectBytes = Base64.getUrlDecoder().decode(request.getAttestationObject());
            byte[] clientDataJSONBytes = Base64.getUrlDecoder().decode(request.getClientDataJSON());

            CollectedClientData clientData = CollectedClientData.fromJson(new String(clientDataJSONBytes, StandardCharsets.UTF_8));
            AttestationObject attObj = new AttestationObject(attestationObjectBytes);

            AuthenticatorAttestationResponse authResponse = new AuthenticatorAttestationResponse(attObj, clientData);

            WebAuthnRegistrationContext context = new WebAuthnRegistrationContext(
                request.getId(),
                attestationObjectBytes,
                clientDataJSONBytes,
                request.getClientExtensionResults(), // this can be an empty map
                grCodeService.getLatestChallenge(), // must match challenge used in options
                null // origin
            );

            WebAuthnRegistrationContextValidationResponse response = registrationValidator.validate(context);

            BiometricCredential credential = BiometricCredential.builder()
                .credentialId(Base64UrlUtil.encodeToString(response.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getCredentialId()))
                .publicKey(Base64.getUrlEncoder().withoutPadding().encodeToString(response.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getCredentialPublicKey()))
                .signCount(response.getAttestationObject().getAuthenticatorData().getSignCount())
                .attestationFormat(attObj.getFormat())
                .deviceType("platform")
                .user(user)
                .build();

            biometricRepo.save(credential);

            return ResponseEntity.ok("Biometric registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }*/

    @PostMapping("/verify")
    public ResponseEntity<?> verifyBiometric(@RequestBody BiometricVerifyRequest request) {
        BiometricCredential credential = biometricRepo.findByCredentialId(request.getCredentialId())
            .orElseThrow(() -> new RuntimeException("Biometric credential not found"));

        if (!credential.getPublicKey().equals(request.getProvidedPublicKey())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid biometric");
        }

        return ResponseEntity.ok("Biometric verified successfully");
    }
}
