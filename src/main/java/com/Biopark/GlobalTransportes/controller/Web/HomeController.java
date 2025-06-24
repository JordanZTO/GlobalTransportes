package com.Biopark.GlobalTransportes.controller.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String mostrarIndex() {
        return "index";
    }
}
