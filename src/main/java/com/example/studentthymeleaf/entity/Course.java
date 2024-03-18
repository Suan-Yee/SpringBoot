package com.example.studentthymeleaf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String status;
    @Column(columnDefinition = "TINYINT")
    private Boolean enabled;
    private String formattedId;

    @OneToMany(mappedBy = "course")
    private Set<Enroll> studentCourse = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @PostLoad
    public void formatId() {
        this.formattedId = String.format("CSR%03d", this.id);
    }

    @PrePersist
    public void prePersist() {
        if (this.enabled == null) {
            enabled = true;
        }
        if(this.status == null){
            status = "publish";
        }
    }
}
