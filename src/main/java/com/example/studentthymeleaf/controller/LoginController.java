package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
  /*  @GetMapping("/login")
    public String showForm(Model model){
        model.addAttribute("user",new User());
        return "login";
    }
*/
    /*@PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        User user1 = userService.findByEmail(user.getEmail());
        log.info("Succussfully login {}",user.getEmail());
        return "redirect:/welcome";
    }*/
    @GetMapping("/")
    public String showWelcomePage() {
      var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Name"+SecurityContextHolder.getContext().getAuthentication().getName());
      if(auth != null && auth.getAuthorities().stream().anyMatch(a ->
              a.getAuthority().equals(User.Role.USER.name()) ||
              a.getAuthority().equals(User.Role.ADMIN.name()))){
          return "welcome";
      }else{
          return "login";
      }

    }
}
