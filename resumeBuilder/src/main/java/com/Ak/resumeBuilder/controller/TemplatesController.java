package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.service.TemplatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/templates")
@Slf4j
public class TemplatesController {
    private final TemplatesService templatesService;
    public ResponseEntity<?> getTemplate(Authentication authentication){

        Map<String,Object> response =templatesService.getTemplates(authentication.getPrincipal());
        return ResponseEntity.ok(response);


    }
}
