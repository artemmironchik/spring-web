package com.example.springweb.controller;

import com.example.springweb.dto.UserDto;
import com.example.springweb.entity.User;
import com.example.springweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {
    private UserService userService;
    @GetMapping("admin/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        log.info("Users: ", users);
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("profile/{id}")
    public String getUserProfile(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Get user profile page");
        User user = userService.findUserById(id);
        log.info("User: ", user);
        redirectAttributes.addFlashAttribute("selectedUser", user);
        return "redirect:/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        log.info("Delete user");
        userService.deleteUser(id);
        log.info("User has been deleted");
        return "redirect:/admin/users";
    }
}
