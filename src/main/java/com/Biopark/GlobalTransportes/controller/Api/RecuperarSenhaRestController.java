package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/senha")
@Tag(name = "Recuperação de Senha", description = "Endpoints para recuperação e redefinição de senha de usuários")
public class RecuperarSenhaRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Solicitar recuperação de senha",
            description = "Envia um link de redefinição de senha para o e-mail informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link de redefinição enviado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "E-mail não encontrado.")
            }
    )
    @PostMapping("/recuperar")
    public ResponseEntity<String> processarRecuperacaoSenha(
            @Parameter(description = "E-mail do usuário que deseja recuperar a senha", required = true)
            @RequestParam String email) {
        boolean enviado = usuarioService.enviarLinkRedefinicaoSenha(email);
        if (enviado) {
            return ResponseEntity.ok("Link de redefinição enviado para seu e-mail.");
        } else {
            return ResponseEntity.badRequest().body("E-mail não encontrado.");
        }
    }

    @Operation(
            summary = "Validar token de redefinição de senha",
            description = "Valida o token enviado por e-mail para redefinir a senha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido."),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado.")
            }
    )
    @GetMapping("/validar-token")
    public ResponseEntity<String> validarToken(
            @Parameter(description = "Token recebido no e-mail de recuperação", required = true)
            @RequestParam String token) {
        boolean valido = usuarioService.validarToken(token);
        if (valido) {
            return ResponseEntity.ok("Token válido.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }

    @Operation(
            summary = "Redefinir senha",
            description = "Permite redefinir a senha de um usuário utilizando um token válido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado.")
            }
    )
    @PostMapping("/redefinir")
    public ResponseEntity<String> processarNovaSenha(
            @Parameter(description = "Token válido de redefinição de senha", required = true)
            @RequestParam String token,
            @Parameter(description = "Nova senha a ser definida", required = true)
            @RequestParam String novaSenha) {
        boolean sucesso = usuarioService.redefinirSenha(token, novaSenha);
        if (sucesso) {
            return ResponseEntity.ok("Senha redefinida com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }
}
