package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {

        return "login";
    }

    @GetMapping("/cadastro")
    public String mostrarCadastro() {
        return "cadastro";
    }

}
