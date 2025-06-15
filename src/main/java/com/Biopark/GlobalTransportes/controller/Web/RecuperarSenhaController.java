package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecuperarSenhaController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/recuperar_senha")
    public String mostrarFormularioRecuperarSenha() {
        return "recuperar_senha";
    }

    @PostMapping("/recuperar_senha")
    public String processarRecuperacaoSenha(@RequestParam String email, Model model) {
        boolean enviado = usuarioService.enviarLinkRedefinicaoSenha(email);
        if (enviado) {
            model.addAttribute("mensagem", "Link de redefinição enviado para seu e-mail.");
        } else {
            model.addAttribute("mensagem", "E-mail não encontrado.");
        }
        return "recuperar_senha";
    }

    @GetMapping("/redefinir_senha")
    public String mostrarFormularioRedefinirSenha(@RequestParam String token, Model model) {
        boolean valido = usuarioService.validarToken(token);
        if (valido) {
            model.addAttribute("token", token);
            return "redefinir_senha";
        } else {
            model.addAttribute("mensagem", "Token inválido ou expirado.");
            return "erro_token";
        }
    }

    @PostMapping("/redefinir_senha")
    public String processarNovaSenha(@RequestParam String token,
                                     @RequestParam String novaSenha,
                                     Model model) {
        boolean sucesso = usuarioService.redefinirSenha(token, novaSenha);
        if (sucesso) {
            model.addAttribute("mensagem", "Senha redefinida com sucesso!");
            return "login";  // Ou redirecione para sua página de login
        } else {
            model.addAttribute("mensagem", "Token inválido ou expirado.");
            return "erro_token";
        }
    }
}
