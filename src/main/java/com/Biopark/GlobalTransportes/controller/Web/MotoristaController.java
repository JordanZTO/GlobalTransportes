package com.Biopark.GlobalTransportes.controller.Web;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Controller
public class MotoristaController {

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

    @PostMapping("/cadastro-motorista")
    public String cadastrarMotorista(
            @ModelAttribute MotoristaDTO dto,
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

    @PostMapping("/motorista/frete/{id}/aceitar")
    public String aceitarFrete(@PathVariable Long id) {
        motoristaService.aceitarFrete(id);
        return "redirect:/motorista/fretes_disponiveis";
    }


    @GetMapping("/cadastro-motorista")
    public String mostrarFormulario(Model model) {
        model.addAttribute("motorista", new MotoristaDTO());
        return "cadastro-motorista";
    }

    @GetMapping("/motorista/home")
    public String mostrarHomeMotorista(@RequestParam(name = "filtro", required = false) String filtro, Model model) {
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        List<Frete> fretesMotorista;

        if (filtro != null && !filtro.isEmpty()) {
            fretesMotorista = freteService.buscarFretesDoMotoristaPorFiltro(motorista.getMotorista_id(), filtro);
        } else {
            fretesMotorista = freteService.listarFretesDoMotoristaLogado();
        }

        model.addAttribute("fretesMotorista", fretesMotorista);
        model.addAttribute("filtro", filtro);
        model.addAttribute("motorista", motorista);
        return "motorista/home";
    }

    @GetMapping("/motorista/fretes_disponiveis")
    public String listarFretesDisponiveis(@RequestParam(name = "filtro", required = false) String filtro, Model model) {
        List<Frete> fretesPendentes;

        if (filtro != null && !filtro.isEmpty()) {
            fretesPendentes = freteService.buscarFretesPendentesPorFiltro(filtro);
        } else {
            fretesPendentes = freteRepository.findByFreteStatusNome("PENDENTE");
        }

        model.addAttribute("fretes", fretesPendentes);
        model.addAttribute("filtro", filtro);
        return "motorista/fretes_disponiveis";
    }

    @GetMapping("/motorista/frete/{id}")
    public String exibirDetalhesFrete(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);
        Motorista motorista = motoristaService.buscarMotoristaLogado();

        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);

        model.addAttribute("frete", frete);
        model.addAttribute("motoristaValido", motorista.isValido());
        model.addAttribute("freteStatus", freteStatusRepository.findAll());
        model.addAttribute("checkpoints", checkpoints);

        return "motorista/detalhes_frete";
    }

    @GetMapping("/motorista/frete/disponivel{id}")
    public String exibirDetalhesFreteDisponivel(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        model.addAttribute("frete", frete);
        model.addAttribute("motoristaValido", motorista.isValido());
        model.addAttribute("freteStatus", freteStatusRepository.findAll());
        return "motorista/detalhes_frete_disponivel";
    }

    @GetMapping("/motorista/frete/checkpoint/{id}")
    public String exibirDetalhesFreteCheckpoint(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);
        List<FreteStatus> statusList = freteStatusRepository.findAll();

        // Filtra os status permitidos para o motorista
        List<FreteStatus> statusPermitidos = statusList.stream()
                .filter(status -> status.getNome().equals("COLETADO") || status.getNome().equals("EM TRANSITO") || status.getNome().equals("EXTRAVIADO") || status.getNome().equals("CANCELADO") || status.getNome().equals("ENTREGUE"))
                .toList();

        model.addAttribute("frete", frete);
        model.addAttribute("freteStatus", statusPermitidos);
        return "motorista/frete_checkpoint";
    }

    @PostMapping("/motorista/frete/{id}/checkpoint")
    public String cadastrarCheckpoint(@PathVariable Long id,
                                      @ModelAttribute FreteCheckpointDTO dto,
                                      RedirectAttributes redirectAttributes) {
        dto.setFreteId(id);
        freteService.cadastrarCheckpoint(dto);
        redirectAttributes.addFlashAttribute("mensagem", "Checkpoint adicionado com sucesso!");
        return "redirect:/motorista/frete/" + id;
    }

    @GetMapping("/motorista/perfil")
    public String exibirPerfilMotorista(Model model) {
        Motorista motorista = motoristaService.buscarMotoristaLogado();

        MotoristaDTO dto = motoristaService.obterDadosParaEdicao(motorista);

        model.addAttribute("motorista", dto);
        return "motorista/perfil";
    }


    @GetMapping("/motorista/perfil/editar")
    public String editarPerfil(Model model) {
        Motorista motorista = motoristaService.buscarMotoristaLogado();
        MotoristaDTO dto = motoristaService.obterDadosParaEdicao(motorista);
        model.addAttribute("motorista", dto);
        return "motorista/editar_perfil";
    }

    @GetMapping("/imagem/{nome:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servirImagem(@PathVariable String nome) {
        try {
            Path caminhoImagem = Paths.get("uploads/imagens").resolve(nome).normalize();
            Resource recurso = new UrlResource(caminhoImagem.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            String tipoMime = Files.probeContentType(caminhoImagem);
            if (tipoMime == null) {
                tipoMime = "application/octet-stream";
            }

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

    @PostMapping("/motorista/editar-perfil")
    public String atualizarPerfil(@ModelAttribute MotoristaDTO dto,
                                  @RequestParam("fotoFrente") MultipartFile fotoFrente,
                                  @RequestParam("fotoPlaca") MultipartFile fotoPlaca,
                                  @RequestParam("fotoCnh") MultipartFile fotoCnh,
                                  @RequestParam(value = "removerFotoFrente", required = false) String removerFotoFrente,
                                  @RequestParam(value = "removerFotoPlaca", required = false) String removerFotoPlaca,
                                  @RequestParam(value = "removerFotoCnh", required = false) String removerFotoCnh,
                                  RedirectAttributes redirectAttributes) {

        dto.setFotoFrente(fotoFrente);
        dto.setFotoPlaca(fotoPlaca);
        dto.setFotoCnh(fotoCnh);

        dto.setRemoverFotoFrente(removerFotoFrente != null);
        dto.setRemoverFotoPlaca(removerFotoPlaca != null);
        dto.setRemoverFotoCnh(removerFotoCnh != null);

        motoristaService.atualizarPerfil(dto);

        redirectAttributes.addFlashAttribute("mensagem", "Perfil atualizado com sucesso!");
        return "redirect:/motorista/perfil";
    }

    @GetMapping("/motorista/suporte")
    public String exibirFormulario(Model model) {
        model.addAttribute("contatoDTO", new ContatoDTO());
        return "motorista/suporte";
    }

    @PostMapping("/motorista/suporte")
    public String enviarMensagem(@ModelAttribute ContatoDTO contatoDTO, Authentication authentication, Model model) {
        String emailUsuario = authentication.getName();

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
                model.addAttribute("erro", "Usuário não encontrado!");
                return "motorista/suporte";
            }
        }

        emailService.enviarEmail(contatoDTO.getAssunto(), contatoDTO.getDetalhes(), nomeRemetente, emailRemetente);

        model.addAttribute("mensagem", "Sua mensagem foi enviada com sucesso!");
        return "motorista/suporte";
    }

}
