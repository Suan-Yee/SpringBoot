package com.example.studentthymeleaf.controller;

import com.example.studentthymeleaf.entity.User;
import com.example.studentthymeleaf.helper.Validator;
import com.example.studentthymeleaf.service.impl.EmailService;
import com.example.studentthymeleaf.service.impl.UserService;
import com.example.studentthymeleaf.service.impl.VerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final VerifyService verifyService;

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "user/user_reg";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("confirmPass") String confirmPass, @ModelAttribute("user") User user, Model model) {

        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Fields cannot be null");
            return "user/user_reg";
        }
        if (userService.isEmailUsed(user.getEmail()) > 0) {
            model.addAttribute("error", "Email already used");
            return "user/user_reg";
        }
        if (!Validator.isValidPassword(user.getPassword())) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.");
            return "user/user_reg";
        }
        if (!user.getPassword().equals(confirmPass)) {
            model.addAttribute("error", "Password does not match");
            return "user/user_reg";
        }
        /*HttpSession session = request.getSession(false);
        User log_user = (User) session.getAttribute("valid_user");*/
        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        if (user.role == null) {
            user.setRole(User.Role.USER);
        }
        user.setEnabled(false);
        User savedUser = userService.createUser(user);
        String token = UUID.randomUUID().toString();
        verifyService.createUrl(token,savedUser);
        emailService.sendHtmlEmail(user.getUsername(),savedUser.getEmail(),token);
        return "user/emailsend";
    }
    @GetMapping("/confirmAccount")
    public String confirmAccount(@RequestParam("token") String url, Model model) {
        User user = verifyService.getUserByVerificationUrl(url);
        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "user/errorPage";
        }
        userService.openAccount(user.getId());

        model.addAttribute("message", "Your account has been verified successfully. You can now login.");
        return "user/verifyAccount";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
       return userPage(1,model);
    }
    @GetMapping("/userList/{page}")
    public String userPage(@PathVariable("page")int pageNumber,Model model){
        Page<User> pageResult = userService.findActiveUser(pageNumber);
        List<User> userList = pageResult.getContent();

        long totalElements = pageResult.getTotalElements();
        int totalPage = pageResult.getTotalPages();
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalPages",totalPage);
        model.addAttribute("totalElements",totalElements);
        model.addAttribute("users", userList);
        return "/user/user_details";
    }

    @GetMapping("/updateUser")
    public String updateUserForm(@RequestParam("userId") Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user/user_update";
    }

    @PostMapping("/updateUserAction")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        User result = userService.findById(user.getId());
        result.setUsername(user.getUsername());
        userService.createUser(result);
        return "redirect:/user/userList";
    }
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId")Long userId){
        userService.deleteUser(userId);
        return "redirect:/user/userList";
    }

    @GetMapping("/searchUser")
    public String findUser(@RequestParam(name = "userId", required = false) Long userId,
                           @RequestParam(name = "name", required = false) String userName,
                           Model model) {

        Page<User> pageUser;
        if (userId != null || (userName != null && !userName.isEmpty())) {

            pageUser = userService.findByIdOrUserName(userId, userName,1);
            List<User> searchResults = pageUser.getContent();
            if (searchResults.isEmpty()) {
                model.addAttribute("errors", "There is no user found");
            }
            model.addAttribute("users", searchResults);
            model.addAttribute("currentPage",1);
            model.addAttribute("totalPages",pageUser.getTotalPages());
            model.addAttribute("totalElements",pageUser.getTotalElements());
            model.addAttribute("students", searchResults);
        } else {
            Page<User> userPage = userService.findActiveUser(1);
            List<User> users = userPage.getContent();
            model.addAttribute("users", users);
        }
        return "/user/user_details";
    }
    @GetMapping("/checkEmail")
    public String checkEmailForm(Model model){
        model.addAttribute("user",new User());
        return "user/changeEmail";
    }

    @PostMapping ("/checkEmail")
    public String checkEmail(@ModelAttribute("user")User user, Model model){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User result = userService.findByEmail(email);
        if(!user.getEmail().equalsIgnoreCase(result.getEmail()) || !encoder.encode(user.getPassword()).equalsIgnoreCase(result.getPassword())){
            log.info("Password encode: {}",encoder.encode(user.getPassword()));
            model.addAttribute("error","This is not your account!!!");
            return "user/changeEmail";
        }
        return "redirect:/changeEmail";
    }
    @GetMapping("/changeEmail")
    public String changeEmailForm(Model model){
        model.addAttribute("user",new User());
        return "user/change";
    }
    @PostMapping("/changeEmail")
    public String changeAction(@ModelAttribute("user")User user,Model model) {

        return null;
    }
    /*public static String getVerificationUrl(String token) {
        return "localhost:8080/user/confirmAccount?token=" + token;
    }*/
}
