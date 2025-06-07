package com.Attendance_Management_System.Attendence_Logging.Services;

import com.Attendance_Management_System.Attendence_Logging.DTOs.StaffProfileSetupRequest;
import com.Attendance_Management_System.Attendence_Logging.Models.StaffProfile;
import com.Attendance_Management_System.Attendence_Logging.Repositories.StaffProfileRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
public class StaffProfileService {

    private final StaffProfileRepository profileRepo;
    private final Cloudinary cloudinary;

    public StaffProfileService(StaffProfileRepository profileRepo, Cloudinary cloudinary) {
        this.profileRepo = profileRepo;
        this.cloudinary = cloudinary;
    }

    public StaffProfile setupProfile(Long userId, StaffProfileSetupRequest request) {
        StaffProfile profile = profileRepo.findByUserId(userId).orElse(new StaffProfile());
        profile.setUserId(userId);
        profile.setStartTime(request.getStartTime());
        profile.setEndTime(request.getEndTime());
        profile.setSetupComplete(false); // Mark as true after image upload
        return profileRepo.save(profile);
    }

    public StaffProfile uploadFace(Long userId, MultipartFile image) throws IOException {
        StaffProfile profile = profileRepo.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Profile not found"));

        // ====== 1. Save image locally ======
        String folderPath = "C:\\Users\\27842\\Desktop\\attendance-flow-design-system\\face-verification\\faces";
        String fileName = userId + ".jpg";

        Path folder = Paths.get(folderPath);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        Path localFilePath = folder.resolve(fileName);
        Files.copy(image.getInputStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);

        // ====== 2. Upload to Cloudinary ======
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
            "folder", "attendance/faces",
            "public_id", "face_" + userId,
            "overwrite", true,
            "resource_type", "image"
        ));

        String cloudImageUrl = (String) uploadResult.get("secure_url");

        // ====== 3. Update profile ======
        profile.setFaceImagePath(cloudImageUrl); // You can also save both paths if needed
        profile.setSetupComplete(true);

        return profileRepo.save(profile);
    }

}
