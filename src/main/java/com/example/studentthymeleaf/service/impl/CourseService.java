package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.CourseRepo;
import com.example.studentthymeleaf.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepo courseRepo;

    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    public Course findById(Long courseId) {
        return courseRepo.findById(courseId).orElse(null);
    }

    public Course findByName(String name) {
        return courseRepo.findByName(name).orElse(null);
    }

    public boolean deleteCourse(Long courseId) {
        if(courseRepo.existsById(courseId)){
            courseRepo.deleteById(courseId);
            return true;
        }else{
            return false;
        }
    }

    public boolean enableCourse(Long courseId) {
       Course course = courseRepo.findById(courseId).orElse(null);
       if(course != null){
           course.setEnabled(false);
           course.setStatus("delete");
           courseRepo.save(course);
           return true;
       }
        return false;
    }

    public Course updateCourse(Course course,Long courseId) {
        Course course_update = courseRepo.findById(courseId).orElse(null);
        course_update.setName(course.getName());
        course_update.setDescription(course.getDescription());
        return courseRepo.save(course_update);
    }

    public List<Course> selectByStatus(String status) {
        return courseRepo.findByStatusAndEnabledIsTrue(status).orElse(Collections.EMPTY_LIST);

    }

    public List<Course> findAllCourse() {
        List<Course> courseList = courseRepo.findCourseByEnabledIsTrue().orElse(Collections.EMPTY_LIST);
        return courseList ;
    }

    public List<Course> findByIdOrName(Long courseId, String name) {
        return courseRepo.findCoursesByIdOrNameAndEnabledTrue(courseId,name).orElse(Collections.EMPTY_LIST);
    }

    public Course changeStatus(Long courseId) {
        Course course = courseRepo.findById(courseId).orElse(null);

        if (course.getStatus().equalsIgnoreCase("pending")) {
            course.setStatus("publish");
        } else {
            course.setStatus("pending");
        }
        return courseRepo.save(course);
    }
}
