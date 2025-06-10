package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.dto.ClienteDTO;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Endereco;
import com.Biopark.GlobalTransportes.model.TipoUsuario;
import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.ClienteRepository;
import com.Biopark.GlobalTransportes.repository.EnderecoRepository;
import com.Biopark.GlobalTransportes.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    public void cadastrarCliente(CadastroClienteDto dto) {
        if (usuarioService.emailJaCadastrado(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        endereco.setPais(dto.getPais() == null ? "Brasil" : dto.getPais());
        enderecoRepository.save(endereco);

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        TipoUsuario tipo = tipoUsuarioRepository.findByNome("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Tipo CLIENTE não encontrado"));

        usuario.setTipo(tipo);
        usuarioService.cadastrarUsuario(usuario);

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setCnpj(dto.getCnpj());
        cliente.setInscricaoEstadual(dto.getInscricaoEstadual());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmailComercial(dto.getEmailComercial());
        cliente.setEndereco(endereco);
        cliente.setUsuario(usuario);

        clienteRepository.save(cliente);
    }

    public Cliente buscarClienteLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o usuário logado"));
    }

    public ClienteDTO obterDadosParaEdicao() {
        Cliente cliente = buscarClienteLogado();
        Endereco endereco = cliente.getEndereco();

        ClienteDTO dto = new ClienteDTO();
        dto.setNome(cliente.getNome());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setCnpj(cliente.getCnpj());
        dto.setInscricaoEstadual(cliente.getInscricaoEstadual());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmailComercial(cliente.getEmailComercial());

        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        dto.setPais(endereco.getPais());

        return dto;
    }

    public void atualizarCliente(ClienteDTO dto) {
        Cliente cliente = buscarClienteLogado();
        Endereco endereco = cliente.getEndereco();

        cliente.setNome(dto.getNome());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setCnpj(dto.getCnpj());
        cliente.setInscricaoEstadual(dto.getInscricaoEstadual());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmailComercial(dto.getEmailComercial());

        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        endereco.setPais(dto.getPais());

        clienteRepository.save(cliente);
        enderecoRepository.save(endereco);
    }

}

