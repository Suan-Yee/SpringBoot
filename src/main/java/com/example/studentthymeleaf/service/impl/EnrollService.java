package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.EnrollRepo;
import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Enroll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollService {

    private final EnrollRepo enrollRepo;

    public void saveEnroll(Enroll enroll){
        enrollRepo.save(enroll);
    }
    public List<Enroll> findByStudentId(Long studentId){
        return enrollRepo.findByStudentId(studentId).orElse(Collections.EMPTY_LIST);
    }
    public void deleteCourseAndStudent(Long studentId){
        enrollRepo.deleteById(studentId);
    }
}
