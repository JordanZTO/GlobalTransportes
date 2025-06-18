package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.dto.ClienteDTO;
import com.Biopark.GlobalTransportes.dto.ContatoDTO;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.model.FreteCheckpoint;
import com.Biopark.GlobalTransportes.model.Motorista;
import com.Biopark.GlobalTransportes.repository.FreteStatusRepository;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.EmailService;
import com.Biopark.GlobalTransportes.service.FreteService;
import com.Biopark.GlobalTransportes.service.MotoristaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente Controller", description = "Endpoints para operações de cliente, como cadastro, perfil e fretes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FreteService freteService;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Cadastrar novo cliente", description = "Realiza o cadastro de um novo cliente.")
    @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso")
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarCliente(@RequestBody CadastroClienteDto dto) {
        clienteService.cadastrarCliente(dto);
        return ResponseEntity.ok("Cliente cadastrado com sucesso!");
    }

    @Operation(summary = "Excluir frete", description = "Exclui um frete caso ele ainda esteja com status PENDENTE.")
    @DeleteMapping("/frete/{id}")
    public ResponseEntity<String> excluirFrete(
            @Parameter(description = "ID do frete a ser excluído") @PathVariable Long id) {
        freteService.excluirFreteSePendente(id);
        return ResponseEntity.ok("Frete excluído com sucesso!");
    }

    @Operation(summary = "Listar fretes do cliente", description = "Lista todos os fretes do cliente logado, com opção de filtro.")
    @GetMapping("/fretes")
    public ResponseEntity<List<Frete>> listarFretes(
            @Parameter(description = "Filtro opcional por status ou outro critério") @RequestParam(required = false) String filtro) {
        Cliente cliente = clienteService.buscarClienteLogado();
        List<Frete> fretes;

        if (filtro != null && !filtro.isEmpty()) {
            fretes = freteService.buscarFretesDoClientePorFiltro(cliente.getClienteId(), filtro);
        } else {
            fretes = freteService.listarFretesDoClienteLogado();
        }

        return ResponseEntity.ok(fretes);
    }

    @Operation(summary = "Detalhar frete", description = "Retorna detalhes de um frete específico, incluindo checkpoints e status.")
    @ApiResponse(responseCode = "200", description = "Detalhes do frete retornados com sucesso")
    @GetMapping("/frete/{id}")
    public ResponseEntity<?> detalhesFrete(
            @Parameter(description = "ID do frete") @PathVariable Long id) {
        Frete frete = freteService.buscarPorId(id);
        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);

        return ResponseEntity.ok(new Object() {
            public final Frete freteDetalhes = frete;
            public final List<FreteCheckpoint> checkpointsLista = checkpoints;
            public final Object freteStatus = freteStatusRepository.findAll();
        });
    }

    @Operation(summary = "Visualizar perfil do cliente", description = "Retorna os dados do cliente atualmente logado.")
    @GetMapping("/perfil")
    public ResponseEntity<Cliente> perfilCliente() {
        Cliente cliente = clienteService.buscarClienteLogado();
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Obter dados para edição de perfil", description = "Retorna os dados atuais do cliente para edição de perfil.")
    @GetMapping("/perfil/editar")
    public ResponseEntity<ClienteDTO> obterDadosEdicao() {
        ClienteDTO dto = clienteService.obterDadosParaEdicao();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar perfil do cliente", description = "Atualiza as informações de perfil do cliente.")
    @PutMapping("/perfil/editar")
    public ResponseEntity<String> atualizarPerfil(
            @RequestBody ClienteDTO dto) {
        clienteService.atualizarCliente(dto);
        return ResponseEntity.ok("Perfil atualizado com sucesso!");
    }

    @Operation(summary = "Enviar mensagem de suporte", description = "Permite que o cliente ou motorista envie uma mensagem para o suporte.")
    @PostMapping("/suporte")
    public ResponseEntity<String> enviarMensagem(
            @RequestBody ContatoDTO contatoDTO,
            Authentication authentication) {
        String nomeRemetente;
        String emailRemetente;

        try {
            Cliente cliente = clienteService.buscarClienteLogado();
            nomeRemetente = cliente.getNome();
            emailRemetente = cliente.getEmailComercial();
        } catch (Exception e1) {
            try {
                Motorista motorista = motoristaService.buscarMotoristaLogado();
                nomeRemetente = motorista.getNome_completo();
                emailRemetente = motorista.getEmail_comercial();
            } catch (Exception e2) {
                return ResponseEntity.badRequest().body("Usuário não encontrado!");
            }
        }

        emailService.enviarEmail(contatoDTO.getAssunto(), contatoDTO.getDetalhes(), nomeRemetente, emailRemetente);
        return ResponseEntity.ok("Sua mensagem foi enviada com sucesso!");
    }

}
