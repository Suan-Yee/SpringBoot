package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.service.impl.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    /*private final AuthenticationManager authenticationManager;*/

    private final UserService userService;
  /*  @GetMapping("/")
    public String showForm(Model model){
        model.addAttribute("user",new User());
        return "login";
    }
    @PostMapping("/")
    public String login(@ModelAttribute("user") User user, Model model){
        log.info("Succussfully login {}",user.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        User user1 = userService.findByEmail(user.getEmail());
        log.info("Succussfully login {}",user.getEmail());
        return "welcome";
    }*/
    @GetMapping("/")
    public String showWelcomePage(HttpSession httpSession) {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      User user = userService.findByEmail(auth.getName());
      log.info("User name {}",auth.getName());
      if(auth != null && auth.getAuthorities().stream().anyMatch(a ->
              a.getAuthority().equals(User.Role.USER.name()) ||
              a.getAuthority().equals(User.Role.ADMIN.name()))){
          httpSession.setAttribute("username",user.getUsername());
          httpSession.setAttribute("datetime", LocalDateTime.now().toLocalDate());
          httpSession.setAttribute("role",user.getRole().name());
          log.info("User role {}",user.getRole().name());

          httpSession.setAttribute("email",user.getEmail());
          return "redirect:/welcome";
      }else{
          return "login";
      }

    }
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
}
