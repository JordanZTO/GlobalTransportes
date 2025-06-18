package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuário", description = "Endpoints para operações relacionadas a usuários")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Verificar Endpoint de Login",
            description = "Retorna uma mensagem indicando que o endpoint de login está disponível."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endpoint de login disponível")
    })
    @GetMapping("/login")
    public ResponseEntity<String> mostrarLogin() {
        return ResponseEntity.ok("Endpoint de login disponível.");
    }

    @Operation(
            summary = "Verificar Endpoint de Cadastro",
            description = "Retorna uma mensagem indicando que o endpoint de cadastro está disponível."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endpoint de cadastro disponível")
    })
    @GetMapping("/cadastro")
    public ResponseEntity<String> mostrarCadastro() {
        return ResponseEntity.ok("Endpoint de cadastro disponível.");
    }
}
