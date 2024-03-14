package com.example.studentthymeleaf.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean isDelete;

    @Transient
    private String formattedId;

    @PostLoad
    public void formatId() {
        this.formattedId = String.format("USR%03d", this.id);
    }

    @PrePersist
    public void prePersist() {
        if (isDelete == null) {
            isDelete = false;
        }
    }
    @Enumerated(EnumType.STRING)
    public Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private OTP otp;

    public enum Role{
        ADMIN,USER;
    }




}
