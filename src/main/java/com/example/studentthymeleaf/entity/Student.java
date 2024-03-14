package com.example.studentthymeleaf.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dob;
    private String gender;
    private String phone;
    private String education;
    private String imageFile;

    private Boolean isDelete;




    @Transient
    private MultipartFile file;
    @Transient
    private String formattedId;

    @PostLoad
    public void formatId() {
        this.formattedId = String.format("STR%03d", this.id);
    }

    @PrePersist
    public void prePersist() {
        if (isDelete == null) {
            isDelete = false;
        }
    }

    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER)
    private Set<Enroll> studentCourse = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
