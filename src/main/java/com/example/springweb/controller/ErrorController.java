package com.example.springweb.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", e.getMessage());
        log.info("error", e.getMessage());
        return modelAndView;
    }
}
