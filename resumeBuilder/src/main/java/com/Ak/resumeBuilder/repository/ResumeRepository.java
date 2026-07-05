package com.Ak.resumeBuilder.repository;

import com.Ak.resumeBuilder.document.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResumeRepository extends MongoRepository<Resume,String> {
 List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);
}
