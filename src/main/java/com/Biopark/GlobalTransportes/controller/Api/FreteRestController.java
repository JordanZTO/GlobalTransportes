package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.dto.FreteCadastroDTO;
import com.Biopark.GlobalTransportes.service.FreteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fretes")
@Tag(name = "Frete Controller", description = "Endpoints relacionados ao cadastro de fretes")
public class FreteRestController {

    @Autowired
    private FreteService freteService;

    @Operation(
            summary = "Cadastrar um novo frete",
            description = "Recebe os dados de um frete no formato JSON e realiza o cadastro no sistema.",
            requestBody = @RequestBody(
                    description = "Dados do frete a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FreteCadastroDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Frete cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar frete")
            }
    )
    @PostMapping
    public ResponseEntity<String> cadastrarFrete(@org.springframework.web.bind.annotation.RequestBody FreteCadastroDTO dto) {
        try {
            freteService.cadastrarFrete(dto);
            return ResponseEntity.ok("Frete cadastrado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar frete: " + e.getMessage());
        }
    }
}
