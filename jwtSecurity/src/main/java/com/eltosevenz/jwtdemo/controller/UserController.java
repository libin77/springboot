package com.eltosevenz.jwtdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public String userInfo() {
        return "User Info: " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "This is the user dashboard";
    }
}
