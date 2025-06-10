package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    // Cria um novo cliente via JSON (POST)
    @PostMapping
    public ResponseEntity<String> cadastrarCliente(@RequestBody CadastroClienteDto dto) {
        clienteService.cadastrarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso.");
    }

}
