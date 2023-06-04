package com.example.springweb.controller;

import com.example.springweb.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class NavigationController {
    @GetMapping("/")
    public String getIndexPage(){
        log.info("Get index page");
        return "index";
    }

    @GetMapping("/profile")
    public String getHomePage(HttpServletRequest request, Model model){
        UserDto user=(UserDto) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        log.info("Get profile page");
        return "profile";
    }
}
