package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.Instructor;
import com.example.studentthymeleaf.service.impl.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "instructor")
@Slf4j
public class instructorController {

    private final InstructorService instructorService;

    @GetMapping("/instructorReg")
    public String registerForm(Model model){
        model.addAttribute("instructor",new Instructor());
        return "instructor/instructor_reg";
    }
    @PostMapping("/instructorReg")
    public String register(@ModelAttribute("instructor")Instructor instructor){
        instructorService.saveInstructor(instructor);
        return "/welcome";
    }
}
