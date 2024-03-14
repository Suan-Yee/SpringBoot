package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.service.impl.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "course")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courseRegistration")
    public String regForm(Model model){
        model.addAttribute("course",new Course());
        return "course/course_reg";
    }
    @PostMapping("/courseRegistration")
    public String register(@ModelAttribute("course")Course course,Model model){
        courseService.createCourse(course);
        return "redirect:/course/courseList";
    }

    @GetMapping("/courseList")
    public String showList(Model model){
        List<Course> courseList = courseService.findAllCourse();
        model.addAttribute("courses",courseList);
        return "course/course_details";
    }
    @GetMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("courseId")Long courseId){

        courseService.enableCourse(courseId);
        return "redirect:/course/courseList";
    }
    @GetMapping("/changeStatus")
    public String changeStatus(@RequestParam("courseId")Long courseId){
        courseService.changeStatus(courseId);
        return "redirect:/course/courseList";
    }
    @GetMapping("/updateCourse")
    public String updateCourseForm(@RequestParam("courseId")Long courseId,Model model){
        Course course = courseService.findById(courseId);
        model.addAttribute("course",course);
        return "course/course_update";
    }
    @PostMapping("/updateCourse")
    public String updateCourse(@ModelAttribute("course")Course course,@RequestParam(name = "hiddenId",required = false)Long courseId){
        courseService.updateCourse(course,courseId);
        return "redirect:/course/courseList";
    }
    @GetMapping("/searchCourse")
    public String findCourse(@RequestParam(name = "courseId",required = false)Long courseId,
                             @RequestParam(name = "name",required = false)String courseName,Model model){

        List<Course> courses;
        if(courseId != null  || (courseName != null && !courseName.isEmpty())){
            courses = courseService.findByIdOrName(courseId,courseName);
            if(courses.isEmpty()){
                model.addAttribute("error","There is no course with search result");
            }
        }else{
            courses = courseService.findAllCourse();
        }
        model.addAttribute("courses",courses);
        return "course/course_details";
    }
}
