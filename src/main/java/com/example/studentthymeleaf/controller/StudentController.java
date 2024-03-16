package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Enroll;
import com.example.studentthymeleaf.entity.Student;
import com.example.studentthymeleaf.form.StudentRegister;
import com.example.studentthymeleaf.service.impl.CourseService;
import com.example.studentthymeleaf.service.impl.EnrollService;
import com.example.studentthymeleaf.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

        return studentPage(1,model);
    }
    @GetMapping("/studentDetails/{page}")
    public String studentPage(@PathVariable("page")int pageNumber,Model model){

            Page<Student> studentPage = studentService.findActiveStudent(pageNumber);
            List<Student> studentList = studentPage.getContent();
            long totalElements = studentPage.getTotalElements();
            int totalPage = studentPage.getTotalPages();
            model.addAttribute("currentPage",pageNumber);
            model.addAttribute("totalPages",totalPage);
            model.addAttribute("totalElements",totalElements);
            model.addAttribute("students",studentList);
            return "student/student_details";
    }
    @GetMapping("/updateStudent")
    public String updateStudentForm(@RequestParam("studentId")Long studentId,Model model){
        Student student = studentService.findById(studentId);

        List<Long> courseList = enrollService.findByStudentId(studentId).stream().map(Course::getId).collect(Collectors.toList());

        /*student.setCourseList();*/
        List<Course> courses = courseService.findAllCourse();
        StudentRegister st = new StudentRegister();
        student.setCourseList(courseList);
        st.setStudent(student);
        model.addAttribute("test",student);
        model.addAttribute("courses",courses);
        model.addAttribute("check",courseList);
        model.addAttribute("student",st);
        return "student/student_update";
    }
    @PostMapping("updateStudent")
    public String updateStudent(@ModelAttribute("student")StudentRegister form,@RequestParam("student.id")Long hiddenId,
                                @RequestParam("courses")List<Long> returnCourse,Model model){
        Student result = form.getStudent();
        form.setSelectedCourses(returnCourse);
        List<Long> courses;
        if(form.getSelectedCourses() == null){
            courses = Collections.EMPTY_LIST;
        }else{
            courses = form.getSelectedCourses();
        }
        studentService.updateStudent(result);
        enrollService.deleteCourseAndStudent(hiddenId);
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

        Page<Student> pageResult;
        if (studentId != null || (name != null && !name.isEmpty()) || (course != null && !course.isEmpty())) {

            pageResult = studentService.searchStudent(studentId, name,course);
            List<Student> searchResults = pageResult.getContent();
            if (searchResults.isEmpty()) {
                model.addAttribute("errors", "There is no student found");
            }
            long totalElements = pageResult.getTotalElements();
            int totalPage = pageResult.getTotalPages();
            model.addAttribute("currentPage",1);
            model.addAttribute("totalPages",totalPage);
            model.addAttribute("totalElements",totalElements);
            model.addAttribute("students", searchResults);
        } else {
            Page<Student> studentPage = studentService.findActiveStudent();
            List<Student> students = studentPage.getContent();
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
    @GetMapping("/studentProfile")
    public String studentDetails(@RequestParam("studentId")Long studentId,Model model){
       Student student = studentService.findById(studentId);
       model.addAttribute("student",student);
       List<String> courseName = enrollService.findByStudentId(studentId)
               .stream().map(Course::getName).collect(Collectors.toList());
       model.addAttribute("courses",courseName);
       return "/student/details";
    }
}
