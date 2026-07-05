package com.Ak.resumeBuilder.service;

import com.Ak.resumeBuilder.document.Resume;
import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.dtos.CreateResume;
import com.Ak.resumeBuilder.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final AuthService authService;
    public Resume createResume(CreateResume request, Object principalObject) {
        Resume newResume= new Resume();

        AuthResponse response= authService.getProfile(principalObject);
        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());
        
        setDefaultData(newResume);

       return  resumeRepository.save(newResume);
        

    }

    private void setDefaultData(Resume newResume) {
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setCertifications(new ArrayList<>());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducations(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setTemplate(new Resume.Template());
        newResume.setSkills(new ArrayList<>());

        newResume.setLanguages(new ArrayList<>());



    }

    public List<Resume> getUserResume(Object principal) {
        AuthResponse response= authService.getProfile(principal);
        return resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());
    }
}
