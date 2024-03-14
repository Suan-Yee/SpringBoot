package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.InstructorRepo;
import com.example.studentthymeleaf.entity.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepo instructorRepo;

    public Instructor saveInstructor(Instructor instructor){
        return instructorRepo.save(instructor);
    }
}
