package com.example.studentthymeleaf.Repository;

import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.entity.VerifyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyRepo extends JpaRepository<VerifyAccount,Long> {
    Optional<VerifyAccount> findByUrl (String token);
}
