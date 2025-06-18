package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.dto.MotoristaDTO;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.model.FreteCheckpoint;
import com.Biopark.GlobalTransportes.model.Motorista;
import com.Biopark.GlobalTransportes.repository.ClienteRepository;
import com.Biopark.GlobalTransportes.repository.FreteStatusRepository;
import com.Biopark.GlobalTransportes.service.AdminService;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.FreteService;
import com.Biopark.GlobalTransportes.service.MotoristaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "Endpoints de administração para clientes, motoristas e fretes")
public class AdminRestController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private FreteService freteService;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Obter dados do dashboard", description = "Retorna estatísticas e indicadores gerais do sistema")
    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard() {
        return ResponseEntity.ok(new Object() {
            public final long totalClientes = adminService.getTotalClientes();
            public final long clientesPendentes = adminService.getClientesPendentesValidacao();
            public final long totalMotoristas = adminService.getTotalMotoristas();
            public final long motoristasPendentes = adminService.getMotoristasPendentesValidacao();
            public final long totalCaminhoes = adminService.getTotalCaminhoes();
            public final long totalFretes = adminService.getTotalFretes();
            public final Object fretesPorStatus = adminService.getFretesPorStatus();
            public final long fretesNoMes = adminService.getFretesCriadosNoMesAtual();
            public final double valorTotalFretes = adminService.getValorTotalMovimentado();
            public final Object topMotoristas = adminService.getTopMotoristasPorFretes();
            public final Object topClientes = adminService.getTopClientesPorFretes();
            public final Object topCidadesOrigem = adminService.getTopCidadesOrigem();
            public final Object topCidadesDestino = adminService.getTopCidadesDestino();
            public final Object fretesPorEstado = adminService.getFretesEmAndamentoPorEstado();
        });
    }

    @Operation(summary = "Listar todos os clientes")
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Detalhar cliente por ID")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> detalhesCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        return clienteOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Validar cliente (Ativar/Desativar)")
    @PutMapping("/cliente/{id}/validar")
    public ResponseEntity<String> validarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Parameter(description = "Se o cliente será válido ou não") @RequestParam boolean valido) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setValido(valido);
        clienteRepository.save(cliente);
        return ResponseEntity.ok("Status de validação do cliente atualizado com sucesso.");
    }

    @Operation(summary = "Listar todos os motoristas")
    @GetMapping("/motoristas")
    public ResponseEntity<List<Motorista>> listarMotoristas() {
        List<Motorista> motoristas = motoristaService.listarTodos();
        return ResponseEntity.ok(motoristas);
    }

    @Operation(summary = "Detalhar motorista por ID")
    @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    @GetMapping("/motorista/{id}")
    public ResponseEntity<?> detalhesMotorista(
            @Parameter(description = "ID do motorista") @PathVariable Long id) {
        Motorista motorista = motoristaService.buscarPorId(id);
        if (motorista == null) {
            return ResponseEntity.notFound().build();
        }
        MotoristaDTO dto = motoristaService.obterDadosParaEdicao(motorista);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Validar motorista (Ativar/Desativar)")
    @PutMapping("/motorista/{id}/validar")
    public ResponseEntity<String> validarMotorista(
            @Parameter(description = "ID do motorista") @PathVariable Long id,
            @Parameter(description = "Se o motorista será válido ou não") @RequestParam boolean valido) {
        Motorista motorista = motoristaService.buscarPorId(id);
        if (motorista == null) {
            return ResponseEntity.notFound().build();
        }

        motorista.setValido(valido);
        motoristaService.salvar(motorista);
        return ResponseEntity.ok("Status de validação do motorista atualizado com sucesso.");
    }

    @Operation(summary = "Listar fretes com filtro opcional")
    @GetMapping("/fretes")
    public ResponseEntity<List<Frete>> listarFretes(
            @Parameter(description = "Filtro opcional por status ou outro critério") @RequestParam(required = false) String filtro) {
        List<Frete> fretes;
        if (filtro != null && !filtro.isEmpty()) {
            fretes = freteService.buscarFretesPorFiltro(filtro);
        } else {
            fretes = freteService.listarTodos();
        }
        return ResponseEntity.ok(fretes);
    }

    @Operation(summary = "Detalhar frete por ID")
    @ApiResponse(responseCode = "404", description = "Frete não encontrado")
    @GetMapping("/frete/{id}")
    public ResponseEntity<?> detalhesFrete(
            @Parameter(description = "ID do frete") @PathVariable Long id) {
        Frete frete = freteService.buscarPorId(id);
        if (frete == null) {
            return ResponseEntity.notFound().build();
        }

        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);

        return ResponseEntity.ok(new Object() {
            public final Frete freteDetalhes = frete;
            public final List<FreteCheckpoint> checkpointsLista = checkpoints;
            public final Object freteStatus = freteStatusRepository.findAll();
        });
    }
}
