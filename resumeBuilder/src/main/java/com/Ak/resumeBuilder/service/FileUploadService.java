package com.Ak.resumeBuilder.service;

import com.Ak.resumeBuilder.document.Resume;
import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.exception.FileUploadException;
import com.Ak.resumeBuilder.repository.ResumeRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    private final AuthService authService;

    private final Cloudinary cloudinary;
    private final ResumeRepository resumeRepository;

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
            log.info("inside filepload-uploadImage()");

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

    public Map<String, String> uploadResumeImages(String id, Object principal, MultipartFile thumbnail, MultipartFile profileImage) {
        AuthResponse response= authService.getProfile(principal);
        Resume existingResume=resumeRepository.findByIdAndUserId(id,response.getId())
                .orElseThrow(()->new RuntimeException("Resume nt found"));
        Map<String,String > returnValue=new HashMap<>();
        Map<String,String> uploadResult;
        if(Objects.nonNull(profileImage)){
             uploadResult=uploadSingleImage(thumbnail);
            returnValue.put("thumbnailLink",uploadResult.get("imageUrl"));
            existingResume.setThumbnailLink(uploadResult.get("imageUrl"));
        }
    uploadResult=uploadSingleImage(profileImage);
    if(Objects.isNull(existingResume.getProfileInfo())){
        existingResume.setProfileInfo(new Resume.ProfileInfo());
    }

        existingResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
        returnValue.put("profilePreviewUrl",uploadResult.get("imageUrl"));

        resumeRepository.save(existingResume);
        returnValue.put("message","Images uploaded successfully");
        return returnValue;
    }
}
