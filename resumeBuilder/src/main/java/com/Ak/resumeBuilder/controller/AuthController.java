package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.dtos.RegisterRequest;
import com.Ak.resumeBuilder.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
   private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
//    @GetMapping("/verify-email")
//    public ResponseEntity<?> verifyEmail(@RequestParam String token){
//        authService.verifyEmail(token);
//        return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("message","email verified successfully"));
//    }
}
