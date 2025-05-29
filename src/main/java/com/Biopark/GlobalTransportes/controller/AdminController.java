package com.Biopark.GlobalTransportes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/home")
    public String mostrarHomeAdmin() {
        return "admin/home"; // aponta para templates/admin/home.html
    }
}
