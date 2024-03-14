package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Enroll;
import com.example.studentthymeleaf.entity.Student;
import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.form.StudentRegister;
import com.example.studentthymeleaf.service.impl.CourseService;
import com.example.studentthymeleaf.service.impl.EnrollService;
import com.example.studentthymeleaf.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "student")
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollService enrollService;

    @GetMapping("/studentRegistration")
    public String registerForm(Model model){
        List<Course> courseList = courseService.selectByStatus("publish");
        StudentRegister st = new StudentRegister();
        model.addAttribute("student",st);
        model.addAttribute("courses",courseList);
        log.info("Courses {}",courseList);
        return "student/student_reg";
    }
    @PostMapping("/studentRegistration")
    public String registerForm(@ModelAttribute("student")StudentRegister form,Model model){
        Student student = form.getStudent();
        List<Long> courses = form.getSelectedCourses();
        studentService.saveStudent(student);
        return save(student, courses);

    }
    @GetMapping("/studentDetails")
    public String studentDetails(Model model){
        List<Student> studentList = studentService.findActiveStudent();
        model.addAttribute("students",studentList);
        log.info("Current userName {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return "student/student_details";
    }
    @GetMapping("/updateStudent")
    public String updateStudentForm(@RequestParam("studentId")Long studentId,Model model){
        Student student = studentService.findById(studentId);
        List<Course> courseList = enrollService.findByStudentId(studentId)
                .stream().map(Enroll::getCourse).collect(Collectors.toList());
        List<Course> courses = courseService.findAllCourse();
        StudentRegister st = new StudentRegister();
        st.setStudent(student);
        model.addAttribute("test",student);
        model.addAttribute("courses",courses);
        model.addAttribute("check",courseList);
        model.addAttribute("student",st);
        return "student/student_update";
    }
    @PostMapping("updateStudent")
    public String updateStudent(@ModelAttribute("student")StudentRegister form,Model model){
        Student result = form.getStudent();
        List<Long> courses;
        if(form.getSelectedCourses() == null){
            courses = Collections.EMPTY_LIST;
        }else{
            courses = form.getSelectedCourses();
        }
        studentService.updateStudent(result);
        enrollService.deleteCourseAndStudent(result.getId());
        return save(result, courses);
    }
    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("studentId")Long studentId){
       Student student = studentService.findById(studentId);
        student.setIsDelete(true);
        studentService.saveStudent(student);
        return "redirect:/student/studentDetails";
    }
    @GetMapping("/searchStudent")
    public String searchStudent(@RequestParam(name = "studentId",required = false)Long studentId,
                                @RequestParam(name = "name",required = false)String name,
                                @RequestParam(name = "course",required = false)String course,Model model){

        List<Student> searchResults;
        if (studentId != null || (name != null && !name.isEmpty()) || (course != null && !course.isEmpty())) {

            searchResults = studentService.searchStudent(studentId, name,course);
            if (searchResults.isEmpty()) {
                model.addAttribute("errors", "There is no student found");
            }
            model.addAttribute("students", searchResults);
        } else {
            List<Student> students = studentService.findAllStudent();
            model.addAttribute("students", students);
        }
        return "/student/student_details";

    }
    private String save(Student result, List<Long> courses) {
        if(courses != null){
            for(Long course : courses) {
                Enroll enroll = new Enroll();
                enroll.setStudent(result);
                enroll.setCourse(courseService.findById(course));
                enroll.setEnroll_date(LocalDateTime.now());
                enrollService.saveEnroll(enroll);
            }
            }
        return "redirect:/student/studentDetails";

    }
}
