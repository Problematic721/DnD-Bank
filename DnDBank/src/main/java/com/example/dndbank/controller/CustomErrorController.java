package com.example.dndbank.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "errors/error";
    }
    
    @GetMapping("/404")
    public String handle404Error() {
        return "errors/404";
    }
}