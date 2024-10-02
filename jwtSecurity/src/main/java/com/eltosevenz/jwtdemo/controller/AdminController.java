package com.eltosevenz.jwtdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/info")
    public String adminInfo() {
        return "Admin Info: " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "This is the admin dashboard";
    }
}
