package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.document.Resume;
import com.Ak.resumeBuilder.dtos.CreateResume;
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

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;

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

    @PutMapping("/upload-image/{id}")
    public ResponseEntity<?> updateResume(@PathVariable String id,
                                          @RequestBody Resume updatedData){
        return null;
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id,
                                                @RequestPart(value = "thumbnail",required = true)MultipartFile thumbnail,
                                                @RequestPart(value = "profileImage",required = false) MultipartFile profileImage,
                                                HttpServlet request) {
        return null;
    }
}
