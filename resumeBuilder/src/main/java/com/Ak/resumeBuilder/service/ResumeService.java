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

    public Resume getResumeById(String resumeId, Object principal) {
        AuthResponse response= authService.getProfile(principal);
        Resume existingResume= resumeRepository.findByIdAndUserId(resumeId,response.getId())
                 .orElseThrow(()->new RuntimeException("resume not find"));
        return existingResume;

    }

    public Resume updateResume(String id, Resume updatedData, Object principal) {
        AuthResponse response=authService.getProfile(principal);
        Resume existingResume=resumeRepository.findByIdAndUserId(response.getId(),response.getId())
                .orElseThrow(()-> new RuntimeException("resume not find"));
        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplate(updatedData.getTemplate());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setContactInfo(updatedData.getContactInfo());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setWorkExperiences(updatedData.getWorkExperiences());
        existingResume.setEducations(updatedData.getEducations());
        existingResume.setLanguages(updatedData.getLanguages());
        existingResume.setInterests(updatedData.getInterests());


        resumeRepository.save(existingResume);
        return existingResume;



    }

    public void deleteResume(String id, Object principal) {
        AuthResponse response= authService.getProfile(principal);
        Resume existingResume=resumeRepository.findByIdAndUserId(id,response.getId())
                .orElseThrow(()->new RuntimeException("Resume not found"));
        resumeRepository.delete(existingResume);




    }
}
