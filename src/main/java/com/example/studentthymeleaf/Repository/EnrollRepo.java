package com.example.studentthymeleaf.Repository;

import com.example.studentthymeleaf.entity.Enroll;
import com.example.studentthymeleaf.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollRepo extends JpaRepository<Enroll,Long> {
    Optional<List<Enroll>> findByStudentId(Long studentId);

    void deleteByStudentId(Long studentId);
}
