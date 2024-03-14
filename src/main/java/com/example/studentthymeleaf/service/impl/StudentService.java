package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.StudentRepo;
import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Enroll;
import com.example.studentthymeleaf.entity.Student;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;

    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }


    public Student findById(Long studentId) {
        return studentRepo.findById(studentId).orElse(null);
    }


    public List<Student> findByName(String name) {
        return studentRepo.findByName(name).orElse(Collections.EMPTY_LIST);
    }


    public List<Student> findAllStudent() {
        List<Student> studentList = studentRepo.findAll();
        if(studentList.isEmpty()){
            return null;
        }
        return studentList;
    }


    public Student updateStudent(Student student) {
        Student updateStudent = studentRepo.findById(student.getId()).orElse(null);
        if(updateStudent != null){
            updateStudent.setName(student.getName());
            updateStudent.setDob(student.getDob());
            updateStudent.setGender(student.getGender());
            updateStudent.setEducation(student.getEducation());
            updateStudent.setPhone(student.getPhone());
            updateStudent.setImageFile(student.getImageFile());
            studentRepo.save(updateStudent);
        }
        return updateStudent;
    }

    public boolean deleteStudent(Long studentId) {
        if(studentRepo.existsById(studentId)){
            studentRepo.deleteById(studentId);
            return true;
        }
        return false;
    }
    public List<Student> findActiveStudent(){
        return studentRepo.findByIsDeleteFalse().orElse(Collections.EMPTY_LIST);
    }
    public List<Student> searchStudent(Long id,String name,String course){
        List<Specification<Student>> specifications = new ArrayList<>();
        if (id != null) {
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"),id)));
        }
        if(StringUtils.hasLength(name)){
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"
                            .concat(name.toLowerCase()).concat("%")));
        }
        if(StringUtils.hasLength(course)){
            specifications.add(getCourse(course));
        }
        Specification<Student> studentSpecification = Specification.where(null);
        for(Specification s: specifications){
            studentSpecification = studentSpecification.and(s);
        }
        return studentRepo.findAll(studentSpecification);
    }
    public static Specification<Student> getCourse(String name){
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.hasLength(name)){
                Join<Student, Enroll> studentEnrollJoin =  root.join("studentCourse");
                Join<Enroll, Course> courseJoin = studentEnrollJoin.join("course");
                return criteriaBuilder.like(criteriaBuilder.lower(courseJoin.get("name")),"%"
                        .concat(name.toLowerCase()).concat("%"));
            }else{
                return null;
            }
        };
    }
}
