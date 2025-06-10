package com.Biopark.GlobalTransportes.controller.Web;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.dto.EditarClienteDto;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FreteService freteService;

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
    public String mostrarHomeCliente(Model model) {
        model.addAttribute("fretes", freteService.listarFretesDoClienteLogado());
        Cliente cliente = clienteService.buscarClienteLogado();
        model.addAttribute("cliente", cliente);
        return "cliente/home";
    }

    @GetMapping("/cliente/frete/{id}")
    public String exibirDetalhesFrete(@PathVariable Long id, Model model) {
        Frete frete = freteService.buscarPorId(id);
        model.addAttribute("frete", frete);
        return "cliente/detalhes_frete";
    }

    @GetMapping("/cliente/perfil")
    public String exibirPerfilCliente(Model model) {
        Cliente cliente = clienteService.buscarClienteLogado();
        model.addAttribute("cliente", cliente);
        return "cliente/perfil"; // Página HTML com os dados do cliente
    }

    @GetMapping("/cliente/perfil/editar")
    public String mostrarFormularioEdicao(Model model) {
        EditarClienteDto dto = clienteService.obterDadosParaEdicao();
        model.addAttribute("cliente", dto);
        return "cliente/editar_perfil";
    }

    @PostMapping("/cliente/perfil/editar")
    public String processarEdicaoPerfil(@ModelAttribute("cliente") EditarClienteDto dto) {
        clienteService.atualizarCliente(dto);
        return "redirect:/cliente/perfil";
    }



}
