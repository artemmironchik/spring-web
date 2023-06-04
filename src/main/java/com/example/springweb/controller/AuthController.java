package com.example.springweb.controller;

import com.example.springweb.dto.UserDto;
import com.example.springweb.entity.User;
import com.example.springweb.exception.MainException;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String loginForm(Model model) {
        log.info("Get login page");
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("user") User candidate,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        try {
            UserDto user = userService.login(candidate);
            log.info("User logged into system");
            request.getSession().setAttribute("user", user);
            return "redirect:/profile";
        } catch (MainException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            log.info("error", e.getMessage());
            return "redirect:/login?error";
        }
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        log.info("User has logged out");
        request.getSession().removeAttribute("user");
        return "redirect:/login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        log.info("Get register page");
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto candidateDto,
                               BindingResult result,
                               HttpServletRequest request,
                               Model model) {
        User candidate = UserMapper.convertDtoToEntity(candidateDto);
        User existing = userService.findByEmail(candidate.getEmail());
        if (existing != null) {
            result.rejectValue("email", "error", "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", candidateDto);
            return "register";
        }
        UserDto user = userService.saveUser(candidate);
        request.getSession().setAttribute("user", user);
        log.info("User successfully registered");
        return "redirect:/register?success";
    }
}
