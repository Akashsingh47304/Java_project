package com.Ak.resumeBuilder.service;

import com.Ak.resumeBuilder.exception.FileUploadException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;

    public Map<String, String> uploadSingleImage(MultipartFile file) {


        try {

            if (file.isEmpty()) {
                throw new FileUploadException("Please select an image.");
            }

            Map<String, Object> imageUploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap("resource_type", "image")
                    );
            log.info("inside fileUpload-uploadImage()");

            return Map.of(
                    "imageUrl",
                    imageUploadResult.get("secure_url").toString()
            );

        } catch (IOException e) {

            throw new FileUploadException(
                    "Failed to upload image to Cloudinary.",
                    e

            );

        }
    }
}
