package com.Biopark.GlobalTransportes.controller;

import com.Biopark.GlobalTransportes.model.TipoUsuario;
import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Corrigido aqui
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
