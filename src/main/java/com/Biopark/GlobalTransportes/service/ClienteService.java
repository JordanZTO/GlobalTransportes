package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Endereco;
import com.Biopark.GlobalTransportes.model.TipoUsuario;
import com.Biopark.GlobalTransportes.model.Usuario;
import com.Biopark.GlobalTransportes.repository.ClienteRepository;
import com.Biopark.GlobalTransportes.repository.EnderecoRepository;
import com.Biopark.GlobalTransportes.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // Criar e salvar endereço
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

        // Criar e salvar usuário
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        TipoUsuario tipo = tipoUsuarioRepository.findByNome("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Tipo CLIENTE não encontrado"));

        usuario.setTipo(tipo);
        usuarioService.cadastrarUsuario(usuario); // já criptografa a senha

        // Criar e salvar cliente
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
}
