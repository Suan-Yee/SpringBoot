package com.example.studentthymeleaf.Repository;

import com.example.studentthymeleaf.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepo extends JpaRepository<Student,Long>, JpaSpecificationExecutor<Student> {

    Optional<List<Student>> findByName(String name);
    Optional<Page<Student>> findByIsDeleteFalse(Pageable pageable);
}
