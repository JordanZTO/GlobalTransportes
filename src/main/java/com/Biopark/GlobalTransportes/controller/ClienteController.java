package com.Biopark.GlobalTransportes.controller;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Exibe o formulário de cadastro
    @GetMapping("/cadastro-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new CadastroClienteDto());
        return "cadastro-cliente";
    }

    // Processa o envio do formulário
    @PostMapping("/cadastro-cliente")
    public String processarCadastroCliente(@ModelAttribute("cliente") CadastroClienteDto dto) {
        clienteService.cadastrarCliente(dto);
        return "redirect:/login?cadastro_sucesso";
    }

    //Exibe a pagina de home do cliente
    @GetMapping("/cliente/home")
    public String mostrarHomeCliente() {
        return "cliente/home";
    }
}