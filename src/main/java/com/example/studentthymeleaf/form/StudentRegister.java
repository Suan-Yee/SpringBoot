package com.example.studentthymeleaf.form;

import com.example.studentthymeleaf.entity.Course;
import com.example.studentthymeleaf.entity.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentRegister {

    private List<Long> selectedCourses;
    private Student student;

}
