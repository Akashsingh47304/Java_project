package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.dtos.LoginRequest;
import com.Ak.resumeBuilder.dtos.RegisterRequest;
import com.Ak.resumeBuilder.service.AuthService;

import com.Ak.resumeBuilder.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
   private final AuthService authService;
   private final FileUploadService fileUploadService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("message","email verified successfully"));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestPart("image")MultipartFile file){
        log.info("inside the auth controller-upload image ");
            Map<String,String> result = fileUploadService.uploadSingleImage(file);
            return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws UnsupportedEncodingException {
        AuthResponse response= authService.login(request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        Object principalObject= authentication.getPrincipal();

     AuthResponse currentProfile=  authService.getProfile(principalObject);

     return new ResponseEntity<>(currentProfile,HttpStatus.OK);

    }
}
