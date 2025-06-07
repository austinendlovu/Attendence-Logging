package com.Attendance_Management_System.Attendence_Logging.Services;


import com.Attendance_Management_System.Attendence_Logging.Models.QrCodeData;
import com.Attendance_Management_System.Attendence_Logging.Repositories.QrCodeDataRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Service
public class QrCodeService {

    private final Cloudinary cloudinary;
    private final QrCodeDataRepository qrCodeRepo;
    private final EmailService emailService;

    public QrCodeService(Cloudinary cloudinary, QrCodeDataRepository qrCodeRepo, EmailService emailService) {
        this.cloudinary = cloudinary;
        this.qrCodeRepo = qrCodeRepo;
        this.emailService = emailService;
    }

    public QrCodeData generateAndSendQr(Long userId, String email) throws IOException, WriterException {
        String payload = "CHECKIN|" + userId;

        QRCodeWriter writer = new QRCodeWriter();
        var matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        byte[] pngData = baos.toByteArray();

        Map upload = cloudinary.uploader().upload(pngData, ObjectUtils.emptyMap());
        String qrUrl = upload.get("secure_url").toString();

        QrCodeData code = qrCodeRepo.findByUserId(userId).orElse(new QrCodeData());
        code.setUserId(userId);
        code.setQrCodeUrl(qrUrl);
        qrCodeRepo.save(code);

        emailService.sendQrCodeEmail(email, qrUrl);

        return code;
    }
    public byte[] generateChallenge() {
        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        return Base64.getUrlEncoder().withoutPadding().encode(challenge);
    }

}
