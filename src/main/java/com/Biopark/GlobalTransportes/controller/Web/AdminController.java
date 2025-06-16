package com.Biopark.GlobalTransportes.controller.Web;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    MotoristaService motoristaService;

    @Autowired
    private FreteService freteService;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/home")
    public String mostrarHomeAdmin() {
        return "admin/home";
    }

    @GetMapping("/admin/validar_cliente")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        return "admin/validar_cliente";
    }

    @GetMapping("/admin/cliente/{id}")
    public String exibirDetalhesCliente(@PathVariable Long id, Model model) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            return "admin/detalhes_cliente";
        } else {
            return "redirect:/admin/validar_cliente";
        }
    }

    @PostMapping("/admin/validar_cliente/{id}")
    public String atualizarValidacaoCliente(@PathVariable Long id,
                                            @RequestParam(name = "valido", required = false) String validoCheckbox) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setValido(validoCheckbox != null);

        clienteRepository.save(cliente);

        return "redirect:/admin/validar_cliente";
    }

    @PostMapping("/admin/validar_motorista/{id}")
    public String atualizarValidacaoMotorista(@PathVariable Long id,
                                              @RequestParam(name = "valido", required = false) String validoCheckbox) {
        Motorista motorista = motoristaService.buscarPorId(id);

        if (motorista == null) {
            return "redirect:/admin/validar_motorista";
        }

        motorista.setValido(validoCheckbox != null);
        motoristaService.salvar(motorista);

        return "redirect:/admin/validar_motorista";
    }


    @GetMapping("/admin/validar_motorista")
    public String listarMotoristas(Model model) {
        List<Motorista> motoristas = motoristaService.listarTodos();
        model.addAttribute("motoristas", motoristas);
        return "admin/validar_motorista";
    }

    @GetMapping("/admin/motorista/{id}")
    public String mostrarDetalhesMotorista(@PathVariable Long id, Model model) {
        Motorista motorista = motoristaService.buscarPorId(id);

        if (motorista == null) {
            return "redirect:/admin/motoristas";
        }

        MotoristaDTO dto = motoristaService.obterDadosParaEdicao(motorista);

        model.addAttribute("motorista", dto);
        return "admin/detalhes_motorista";
    }

    @GetMapping("/admin/fretes")
    public String listarFretes(@RequestParam(name = "filtro", required = false) String filtro, Model model) {
        List<Frete> fretes;

        if (filtro != null && !filtro.isEmpty()) {
            fretes = freteService.buscarFretesPorFiltro(filtro);
        } else {
            fretes = freteService.listarTodos();
        }

        model.addAttribute("fretes", fretes);
        model.addAttribute("filtro", filtro);
        return "admin/fretes";
    }

    @GetMapping("/admin/frete/{id}")
    public String exibirDetalhesFrete(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);

        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);

        model.addAttribute("frete", frete);
        model.addAttribute("freteStatus", freteStatusRepository.findAll());
        model.addAttribute("checkpoints", checkpoints);
        return "admin/detalhes_frete";
    }

    @GetMapping("/admin/dashboard")
    public String mostrarDashboard(Model model) {

        model.addAttribute("totalClientes", adminService.getTotalClientes());
        model.addAttribute("clientesPendentes", adminService.getClientesPendentesValidacao());

        model.addAttribute("totalMotoristas", adminService.getTotalMotoristas());
        model.addAttribute("motoristasPendentes", adminService.getMotoristasPendentesValidacao());

        model.addAttribute("totalCaminhoes", adminService.getTotalCaminhoes());

        model.addAttribute("totalFretes", adminService.getTotalFretes());
        model.addAttribute("fretesPorStatus", adminService.getFretesPorStatus());

        model.addAttribute("fretesNoMes", adminService.getFretesCriadosNoMesAtual());
        model.addAttribute("valorTotalFretes", adminService.getValorTotalMovimentado());

        model.addAttribute("topMotoristas", adminService.getTopMotoristasPorFretes());
        model.addAttribute("topClientes", adminService.getTopClientesPorFretes());
        model.addAttribute("topCidadesOrigem", adminService.getTopCidadesOrigem());
        model.addAttribute("topCidadesDestino", adminService.getTopCidadesDestino());
        model.addAttribute("fretesPorEstado", adminService.getFretesEmAndamentoPorEstado());

        return "admin/dashboard";
    }

}
