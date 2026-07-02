package com.Ak.resumeBuilder.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "name is required")
    @Size(min = 2,max = 15 , message = "name must be between 2 and 50 character")
    private String name;
    @NotBlank(message = "password is required")
    @Size(min = 6, max=15,message = "password must be between 6 and 15 characters")
    private String password;
    private String profileImageUrl;

}
