package com.Biopark.GlobalTransportes.controller.Api;

import com.Biopark.GlobalTransportes.dto.ContatoDTO;
import com.Biopark.GlobalTransportes.dto.MotoristaDTO;
import com.Biopark.GlobalTransportes.dto.FreteCheckpointDTO;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.FreteRepository;
import com.Biopark.GlobalTransportes.repository.FreteStatusRepository;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.EmailService;
import com.Biopark.GlobalTransportes.service.FreteService;
import com.Biopark.GlobalTransportes.service.MotoristaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/motorista")
@Tag(name = "Motorista Controller", description = "Endpoints relacionados às operações dos motoristas no sistema")
public class MotoristaRestController {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private FreteRepository freteRepository;

    @Autowired
    private FreteService freteService;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Cadastrar motorista", description = "Cadastra um novo motorista com envio de fotos.")
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarMotorista(@ModelAttribute MotoristaDTO dto,
                                                     @RequestParam("fotoFrente") MultipartFile fotoFrente,
                                                     @RequestParam("fotoPlaca") MultipartFile fotoPlaca,
                                                     @RequestParam("fotoCnh") MultipartFile fotoCnh) {
        dto.setFotoFrente(fotoFrente);
        dto.setFotoPlaca(fotoPlaca);
        dto.setFotoCnh(fotoCnh);
        motoristaService.cadastrarMotorista(dto);
        return ResponseEntity.ok("Motorista cadastrado com sucesso!");
    }

    @Operation(summary = "Aceitar frete", description = "Permite ao motorista aceitar um frete.")
    @PostMapping("/frete/{id}/aceitar")
    public ResponseEntity<String> aceitarFrete(@PathVariable Long id) {
        motoristaService.aceitarFrete(id);
        return ResponseEntity.ok("Frete aceito com sucesso!");
    }

    @Operation(summary = "Listar fretes disponíveis", description = "Lista fretes que ainda estão pendentes.")
    @GetMapping("/fretes-disponiveis")
    public List<Frete> listarFretesDisponiveis(@RequestParam(required = false) String filtro) {
        if (filtro != null && !filtro.isEmpty()) {
            return freteService.buscarFretesPendentesPorFiltro(filtro);
        } else {
            return freteRepository.findByFreteStatusNome("PENDENTE");
        }
    }

    @Operation(summary = "Listar fretes do motorista", description = "Lista os fretes associados ao motorista logado, com filtro opcional.")
    @GetMapping("/home")
    public List<Frete> listarFretesDoMotorista(@RequestParam(required = false) String filtro) {
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        if (filtro != null && !filtro.isEmpty()) {
            return freteService.buscarFretesDoMotoristaPorFiltro(motorista.getMotorista_id(), filtro);
        } else {
            return freteService.listarFretesDoMotoristaLogado();
        }
    }

    @Operation(summary = "Detalhar frete", description = "Retorna os detalhes de um frete, checkpoints e status disponíveis.")
    @GetMapping("/frete/{id}")
    public ResponseEntity<?> detalhesFrete(@PathVariable Long id) {
        Frete frete = freteService.buscarPorId(id);
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);
        return ResponseEntity.ok(new Object() {
            public final Frete freteObj = frete;
            public final boolean motoristaValido = motorista.isValido();
            public final List<FreteStatus> freteStatus = freteStatusRepository.findAll();
            public final List<FreteCheckpoint> checkpointsList = checkpoints;
        });
    }

    @Operation(summary = "Detalhar frete disponível", description = "Exibe informações básicas de um frete ainda disponível.")
    @GetMapping("/frete-disponivel/{id}")
    public ResponseEntity<?> detalhesFreteDisponivel(@PathVariable Long id) {
        Frete frete = freteService.buscarPorId(id);
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        return ResponseEntity.ok(new Object() {
            public final Frete freteObj = frete;
            public final boolean motoristaValido = motorista.isValido();
            public final List<FreteStatus> freteStatus = freteStatusRepository.findAll();
        });
    }

    @Operation(summary = "Ver detalhes de checkpoint", description = "Lista os status permitidos para atualização de um frete (checkpoint).")
    @GetMapping("/frete/checkpoint/{id}")
    public ResponseEntity<?> detalhesCheckpoint(@PathVariable Long id) {
        Frete frete = freteService.buscarPorId(id);
        List<FreteStatus> statusPermitidos = freteStatusRepository.findAll().stream()
                .filter(status -> List.of("COLETADO", "EM TRANSITO", "EXTRAVIADO", "CANCELADO", "ENTREGUE").contains(status.getNome()))
                .toList();

        return ResponseEntity.ok(new Object() {
            public final Frete freteObj = frete;
            public final List<FreteStatus> freteStatus = statusPermitidos;
        });
    }

    @Operation(summary = "Cadastrar checkpoint", description = "Permite ao motorista cadastrar um novo checkpoint para o frete.")
    @PostMapping("/frete/{id}/checkpoint")
    public ResponseEntity<String> cadastrarCheckpoint(@PathVariable Long id,
                                                      @RequestBody FreteCheckpointDTO dto) {
        dto.setFreteId(id);
        freteService.cadastrarCheckpoint(dto);
        return ResponseEntity.ok("Checkpoint adicionado com sucesso!");
    }

    @Operation(summary = "Perfil do motorista", description = "Retorna os dados do motorista logado para visualização.")
    @GetMapping("/perfil")
    public MotoristaDTO perfilMotorista() {
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        return motoristaService.obterDadosParaEdicao(motorista);
    }

    @Operation(summary = "Atualizar perfil do motorista", description = "Atualiza os dados do perfil do motorista, incluindo fotos.")
    @PutMapping("/perfil/editar")
    public ResponseEntity<String> atualizarPerfil(@ModelAttribute MotoristaDTO dto,
                                                  @RequestParam("fotoFrente") MultipartFile fotoFrente,
                                                  @RequestParam("fotoPlaca") MultipartFile fotoPlaca,
                                                  @RequestParam("fotoCnh") MultipartFile fotoCnh,
                                                  @RequestParam(value = "removerFotoFrente", required = false) String removerFotoFrente,
                                                  @RequestParam(value = "removerFotoPlaca", required = false) String removerFotoPlaca,
                                                  @RequestParam(value = "removerFotoCnh", required = false) String removerFotoCnh) {

        dto.setFotoFrente(fotoFrente);
        dto.setFotoPlaca(fotoPlaca);
        dto.setFotoCnh(fotoCnh);
        dto.setRemoverFotoFrente(removerFotoFrente != null);
        dto.setRemoverFotoPlaca(removerFotoPlaca != null);
        dto.setRemoverFotoCnh(removerFotoCnh != null);

        motoristaService.atualizarPerfil(dto);
        return ResponseEntity.ok("Perfil atualizado com sucesso!");
    }

    @Operation(summary = "Servir imagem", description = "Retorna uma imagem (arquivo) salva nos uploads, usada por motoristas.")
    @GetMapping("/imagem/{nome:.+}")
    public ResponseEntity<Resource> servirImagem(@PathVariable String nome) {
        try {
            Path caminhoImagem = Paths.get("uploads/imagens").resolve(nome).normalize();
            Resource recurso = new UrlResource(caminhoImagem.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            String tipoMime = Files.probeContentType(caminhoImagem);
            if (tipoMime == null) tipoMime = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(tipoMime))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Enviar mensagem de suporte", description = "Envia uma mensagem de suporte para a equipe administrativa.")
    @PostMapping("/suporte")
    public ResponseEntity<String> enviarMensagemSuporte(@RequestBody ContatoDTO contatoDTO, Authentication authentication) {
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
