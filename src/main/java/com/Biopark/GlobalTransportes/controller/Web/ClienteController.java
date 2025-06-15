package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.dto.ClienteDTO;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.model.FreteCheckpoint;
import com.Biopark.GlobalTransportes.model.Motorista;
import com.Biopark.GlobalTransportes.repository.FreteStatusRepository;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FreteService freteService;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    @PostMapping("/cadastro-cliente")
    public String processarCadastroCliente(@ModelAttribute("cliente") CadastroClienteDto dto) {
        clienteService.cadastrarCliente(dto);
        return "login";
    }

    @PostMapping("/cliente/frete/{id}/excluir")
    public String excluirFrete(@PathVariable Long id) {
        freteService.excluirFreteSePendente(id);
        return "redirect:/cliente/home";
    }

    @GetMapping("/cadastro-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new CadastroClienteDto());
        return "cadastro-cliente";
    }

    @GetMapping("/cliente/home")
    public String mostrarHomeCliente(@RequestParam(name = "filtro", required = false) String filtro, Model model) {
        Cliente cliente = clienteService.buscarClienteLogado();
        List<Frete> fretes;

        if (filtro != null && !filtro.isEmpty()) {
            fretes = freteService.buscarFretesDoClientePorFiltro(cliente.getClienteId(), filtro);
        } else {
            fretes = freteService.listarFretesDoClienteLogado();
        }

        model.addAttribute("fretes", fretes);
        model.addAttribute("filtro", filtro);
        model.addAttribute("cliente", cliente);
        return "cliente/home";
    }


    @GetMapping("/cliente/frete/{id}")
    public String exibirDetalhesFrete(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);

        List<FreteCheckpoint> checkpoints = freteService.listarCheckpointsPorFrete(id);

        model.addAttribute("frete", frete);
        model.addAttribute("freteStatus", freteStatusRepository.findAll());
        model.addAttribute("checkpoints", checkpoints);
        return "cliente/detalhes_frete";
    }

    @GetMapping("/cliente/perfil")
    public String exibirPerfilCliente(Model model) {
        Cliente cliente = clienteService.buscarClienteLogado();
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }

    @GetMapping("/cliente/perfil/editar")
    public String mostrarFormularioEdicao(Model model) {
        ClienteDTO dto = clienteService.obterDadosParaEdicao();
        model.addAttribute("cliente", dto);
        return "cliente/editar_perfil";
    }

    @PostMapping("/cliente/perfil/editar")
    public String processarEdicaoPerfil(@ModelAttribute("cliente") ClienteDTO dto) {
        clienteService.atualizarCliente(dto);
        return "redirect:/cliente/perfil";
    }



}
