package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.VerifyRepo;
import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.entity.VerifyAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final VerifyRepo verifyRepo;

    public User getUserByVerificationUrl(String token){
        VerifyAccount account = verifyRepo.findByUrl(token).orElse(null);
        if(account != null){
            User user  = account.getUser();
            return user;
        }
        return null;
    }
    public void createUrl(String url,User user){
        VerifyAccount verifyAccount = new VerifyAccount();
        verifyAccount.setUrl(url);
        verifyAccount.setUser(user);
        verifyRepo.save(verifyAccount);
    }
}
