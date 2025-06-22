package com.Biopark.GlobalTransportes.UnitarioService;

import com.Biopark.GlobalTransportes.dto.CadastroClienteDto;
import com.Biopark.GlobalTransportes.dto.ClienteDTO;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.ClienteRepository;
import com.Biopark.GlobalTransportes.repository.EnderecoRepository;
import com.Biopark.GlobalTransportes.repository.TipoUsuarioRepository;
import com.Biopark.GlobalTransportes.service.ClienteService;
import com.Biopark.GlobalTransportes.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosClientes() {
        clienteService.listarTodosClientes();
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveCadastrarClienteQuandoEmailNaoExiste() {
        CadastroClienteDto dto = new CadastroClienteDto();
        dto.setEmail("teste@exemplo.com");
        dto.setNome("João");
        dto.setLogradouro("Rua A");
        dto.setNumero("123");
        dto.setCidade("Cidade");
        dto.setEstado("Estado");
        dto.setCep("00000-000");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(false);
        when(tipoUsuarioRepository.findByNome("CLIENTE")).thenReturn(Optional.of(new TipoUsuario()));

        clienteService.cadastrarCliente(dto);

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(usuarioService, times(1)).cadastrarUsuario(any(Usuario.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        CadastroClienteDto dto = new CadastroClienteDto();
        dto.setEmail("teste@exemplo.com");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clienteService.cadastrarCliente(dto));

        assertEquals("Email já cadastrado.", exception.getMessage());
    }

    @Test
    void deveBuscarClienteLogado() {
        String email = "teste@exemplo.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        when(usuarioService.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarClienteLogado();

        assertEquals(cliente, resultado);
    }

    @Test
    void deveObterDadosParaEdicao() {
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setCnpj("123");
        cliente.setTelefone("9999-9999");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        cliente.setEndereco(endereco);

        // Mock do buscarClienteLogado
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("email@exemplo.com");
        SecurityContextHolder.setContext(securityContext);

        Usuario usuario = new Usuario();
        usuario.setEmail("email@exemplo.com");
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.of(cliente));

        ClienteDTO dto = clienteService.obterDadosParaEdicao();

        assertEquals("João", dto.getNome());
        assertEquals("Rua A", dto.getLogradouro());
    }

    @Test
    void deveAtualizarCliente() {
        Cliente cliente = new Cliente();
        cliente.setEndereco(new Endereco());

        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Novo Nome");
        dto.setLogradouro("Nova Rua");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("email@exemplo.com");
        SecurityContextHolder.setContext(securityContext);

        Usuario usuario = new Usuario();
        usuario.setEmail("email@exemplo.com");

        when(usuarioService.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.of(cliente));

        clienteService.atualizarCliente(dto);

        verify(clienteRepository, times(1)).save(cliente);
        verify(enderecoRepository, times(1)).save(cliente.getEndereco());

        assertEquals("Novo Nome", cliente.getNome());
        assertEquals("Nova Rua", cliente.getEndereco().getLogradouro());
    }
}
