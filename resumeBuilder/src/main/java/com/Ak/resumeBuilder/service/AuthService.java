package com.Ak.resumeBuilder.service;

import com.Ak.resumeBuilder.document.User;
import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.dtos.LoginRequest;
import com.Ak.resumeBuilder.dtos.RegisterRequest;
import com.Ak.resumeBuilder.exception.ResourceExistsException;
import com.Ak.resumeBuilder.repository.UserRepository;
import com.Ak.resumeBuilder.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final EmailService emailService;
    @Value("${app.base.url}:http://localhost:8080")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request){
        log.info("inside AuthService : register() {}",request);
        log.info("Checking if email exists: {}", request.getEmail());


        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User already exist with this email");
        }
        User newUser = toUser(request);
        log.info("Saving user...");
        userRepository.save(newUser);
        log.info("User saved successfully");
//       sendVerificationEmail(newUser);

        log.info("Verification email sent");

        return toResponse(newUser);


    }
//    public void verifyEmail(String token){
//        User user = userRepository.findByToken(token)
//                .orElseThrow(()-> new RuntimeException("Invalid or expires verification token"));
//        if(user.getVerificationExpires()!=null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
//            throw new RuntimeException("verification token  is expired please request newOne");
//        }
//        user.setEmailVerified(true);
//        user.setVerificationToken(null);
//        user.setVerificationExpires(null);
//        userRepository.save(user);
//    }

    public void sendVerificationEmail(User newUser) {
        try {
            String link =appBaseUrl+ "/api/auth/verify-email?token="+newUser.getVerificationToken();
            String html =
                    "<div style='font-family:Arial,sans-serif;padding:20px'>" +
                            "<h2 style='color:#2563eb'>Verify Your Email</h2>" +

                            "<p>Hi <strong>" + newUser.getName() + "</strong>,</p>" +

                            "<p>Thank you for registering with <strong>Resume Builder</strong>.</p>" +

                            "<p>Please click the button below to verify your email address.</p>" +

                            "<p>" +
                            "<a href='" + link + "' " +
                            "style='display:inline-block;" +
                            "padding:12px 20px;" +
                            "background:#2563eb;" +
                            "color:#ffffff;" +
                            "text-decoration:none;" +
                            "border-radius:5px;'>Verify Email</a>" +
                            "</p>" +

                            "<p>If the button doesn't work, copy and paste this link into your browser:</p>" +

                            "<p>" + link + "</p>" +

                            "<p style='color:red;'>This verification link will expire in 24 hours.</p>" +

                            "<hr>" +

                            "<p style='font-size:13px;color:gray;'>" +
                            "If you didn't create this account, you can safely ignore this email." +
                            "</p>" +

                            "<p>Regards,<br><strong>Resume Builder Team</strong></p>" +

                            "</div>";
//                        emailService.sendHtmlEmail(newUser.getEmail(), "verify your email",html);
        }catch (Exception e){
            throw new RuntimeException("Failed to send verifciation email:"+ e.getMessage());
        }
    }

    private AuthResponse toResponse(User newUser){
        return   AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .build();
    }
    private User toUser(RegisterRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();

    }
    public AuthResponse login(LoginRequest loginRequest) throws UnsupportedEncodingException {
        User existingUser= userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("Invalid email or password"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())){
            throw new UsernameNotFoundException("Invalid email or password");
        }
        String jwt =jwtUtils.generateToken(existingUser.getId());
        AuthResponse response= toResponse(existingUser);
       response.setToken(jwt);
       return response;
    }
}


