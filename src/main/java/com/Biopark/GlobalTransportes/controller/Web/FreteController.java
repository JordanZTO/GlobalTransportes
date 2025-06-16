package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.dto.FreteCadastroDTO;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fretes")
public class FreteController {

    @Autowired
    private FreteService freteService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/salvarFrete")
    public String salvarFrete(@ModelAttribute("frete") FreteCadastroDTO dto) {
        freteService.cadastrarFrete(dto);
        return "redirect:/cliente/home";
    }

    @GetMapping("/cadastrar")
    public String exibirFormulario(Model model) {
        model.addAttribute("frete", new FreteCadastroDTO());
        return "cliente/cadastrar_frete";
    }

}
