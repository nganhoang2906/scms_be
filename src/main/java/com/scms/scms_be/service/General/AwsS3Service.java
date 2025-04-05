package com.scms.scms_be.service.General;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AwsS3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String uploadFile(MultipartFile file, Long companyId) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String newFileName = "logo-" + companyId;

        Path tempPath = Files.createTempFile("temp", originalFileName);
        file.transferTo(tempPath.toFile());

        // Thêm quyền PublicRead khi upload file
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFileName, tempPath.toFile())
                .withCannedAcl(CannedAccessControlList.PublicRead); // Set quyền public-read

        amazonS3.putObject(putObjectRequest);
        Files.delete(tempPath);

        return newFileName; // Trả về tên file trên S3
    }
}
