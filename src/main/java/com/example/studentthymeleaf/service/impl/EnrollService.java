package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.EnrollRepo;
import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Enroll;
import com.example.studentthymeleaf.entity.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class EnrollService {

    private final EnrollRepo enrollRepo;

    public void saveEnroll(Enroll enroll){
        enrollRepo.save(enroll);
    }
    public List<Course> findByStudentId(Long studentId){
        List<Enroll> enrollments = enrollRepo.findByStudentId(studentId).orElse(Collections.EMPTY_LIST);
        List<Course> courses = enrollments.stream()
                .map(Enroll::getCourse)
                .collect(Collectors.toList());

        return courses;
    }
    public void deleteCourseAndStudent(Long studentId){
        enrollRepo.deleteByStudentId(studentId);
    }
}
