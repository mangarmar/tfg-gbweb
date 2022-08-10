package com.gbweb.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
 
@RestController
public class ErrorControllerCustom implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/errorPage");
        return modelAndView;
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
    
}