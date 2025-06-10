package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.dto.CadastroMotoristaDto;
import com.Biopark.GlobalTransportes.service.MotoristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MotoristaController {

    @Autowired
    private MotoristaService motoristaService;

    @PostMapping("/cadastro-motorista")
    public String cadastrarMotorista(
            @ModelAttribute CadastroMotoristaDto dto,
            @RequestParam("fotoFrente") MultipartFile fotoFrente,
            @RequestParam("fotoPlaca") MultipartFile fotoPlaca,
            @RequestParam("fotoCnh") MultipartFile fotoCnh
    ) {
        dto.setFotoFrente(fotoFrente);
        dto.setFotoPlaca(fotoPlaca);
        dto.setFotoCnh(fotoCnh);

        motoristaService.cadastrarMotorista(dto);
        return "redirect:/login";
    }

    @GetMapping("/cadastro-motorista")
    public String mostrarFormulario(Model model) {
        model.addAttribute("motorista", new CadastroMotoristaDto());
        return "cadastro-motorista";
    }


    //Exibe a pagina de home do cliente
    @GetMapping("/motorista/home")
    public String mostrarHomeMotorista() {
        return "motorista/home";
    }
}

