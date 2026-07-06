package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.document.Resume;
import com.Ak.resumeBuilder.dtos.CreateResume;
import com.Ak.resumeBuilder.service.FileUploadService;
import com.Ak.resumeBuilder.service.ResumeService;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createResume(@RequestBody CreateResume request, Authentication authentication){
        Resume newResume= resumeService.createResume(request,authentication.getPrincipal());
        return new ResponseEntity<>(newResume, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<?> getUserResume(Authentication authentication){
        List<Resume> resumes= resumeService.getUserResume(authentication.getPrincipal());
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable String id,Authentication authentication){
        Resume resume= resumeService.getResumeById(id,authentication.getPrincipal());
        return ResponseEntity.ok(resume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(@PathVariable String id,
                                          @RequestBody Resume updatedData,
                                          Authentication authentication){
        Resume updatedResume=resumeService.updateResume(id,updatedData,authentication.getPrincipal());
   return ResponseEntity.ok(updatedResume);

    }
    @PutMapping("/upload-image/{id}")
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id,
                                                @RequestPart(value = "thumbnail",required = true)MultipartFile thumbnail,
                                                @RequestPart(value = "profileImage",required = false) MultipartFile profileImage,
                                                HttpServlet request,
                                                Authentication authentication) {
     Map<String,String > response= fileUploadService.uploadResumeImages(id,authentication.getPrincipal(),thumbnail,profileImage);
     return ResponseEntity.ok(response);
    }
    @DeleteMapping("/id")
    public ResponseEntity<?> deleteResume(@PathVariable String id,
                                          Authentication authentication){
       resumeService.deleteResume(id,authentication.getPrincipal());
        return ResponseEntity.ok(
                Map.of(
                        "message", "Resume deleted Successfully",

                )
        );
    }
}
