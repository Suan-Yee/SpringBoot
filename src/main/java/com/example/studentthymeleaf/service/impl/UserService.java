package com.example.studentthymeleaf.service.impl;

import com.example.studentthymeleaf.Repository.UserRepository;
import com.example.studentthymeleaf.entity.AppUser;
import com.example.studentthymeleaf.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public User loginAccount(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword()).orElse(null);
    }
    /*@Transactional
    @EventListener(classes = ContextRefreshedEvent.class)
    public void userInitializer(){
        if(userRepository.count() == 0){
            var user = new User();
            user.setEmail("user@gmail.com");
            user.setPassword(encoder.encode("admin"));
            user.setUsername("user");
            userRepository.save(user);
        }
    }*/

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean changeEmail(Long userId,String email) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setEmail(email);
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> findByName(String name) {
        return userRepository.findByUsername(name).orElse(Collections.EMPTY_LIST);
    }

    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setIsDelete(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> findAll() {
        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            return null;
        }
        return userList;
    }

    public List<User> findByIdOrUserName(Long userId, String name) {
        return userRepository.findActiveUserQuery(userId, name).orElse(Collections.EMPTY_LIST);
    }

    public Integer isEmailUsed(String email) {
       return userRepository.countByEmail(email);
    }

    public String findRoleByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
           return user.getRole().name();
        }
        return null;
    }

    public boolean updatePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setPassword(password);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public List<User> findActiveUser(){
        return userRepository.findByIsDeleteFalse().orElse(Collections.EMPTY_LIST);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
   /*return userRepository.findByEmail(email)
           .map(user -> org.springframework.security.core.userdetails.User.withUsername(email)
                   .password(user.getPassword())
                   .authorities(AuthorityUtils.createAuthorityList(user.getRole().name()))
                   .build())
           .orElseThrow(() -> new UsernameNotFoundException("There is no user with that user email %s".formatted(email)));

    }*/
        User user = userRepository.findByEmail(email).orElse(null);
        return new AppUser(user);
    }
}
