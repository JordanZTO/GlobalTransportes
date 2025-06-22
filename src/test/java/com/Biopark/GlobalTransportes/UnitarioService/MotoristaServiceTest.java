package com.Biopark.GlobalTransportes.UnitarioService;

import com.Biopark.GlobalTransportes.dto.MotoristaDTO;
import com.Biopark.GlobalTransportes.exception.RecursoJaExistenteException;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.*;
import com.Biopark.GlobalTransportes.service.ArquivoService;
import com.Biopark.GlobalTransportes.service.MotoristaService;
import com.Biopark.GlobalTransportes.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MotoristaServiceTest {

    @InjectMocks
    private MotoristaService motoristaService;

    @Mock
    private MotoristaRepository motoristaRepository;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Mock
    private CaminhaoRepository caminhaoRepository;
    @Mock
    private ArquivoService arquivoService;
    @Mock
    private FreteRepository freteRepository;
    @Mock
    private FreteStatusRepository freteStatusRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock de segurança para simular usuário logado
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("motorista@email.com");
        SecurityContextHolder.setContext(securityContext);

        Usuario usuario = new Usuario();
        usuario.setEmail("motorista@email.com");
        when(usuarioService.buscarPorEmail("motorista@email.com")).thenReturn(Optional.of(usuario));

        Motorista motoristaLogado = new Motorista();
        motoristaLogado.setUsuario(usuario);
        motoristaLogado.setValido(true);
        when(motoristaRepository.findByUsuario(usuario)).thenReturn(Optional.of(motoristaLogado));
    }

    @Test
    void deveLancarExcecaoSeEmailJaExistir() {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setEmail("teste@email.com");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(true);

        assertThrows(RecursoJaExistenteException.class, () -> motoristaService.cadastrarMotorista(dto));
    }

    @Test
    void deveLancarExcecaoSeCpfJaExistir() {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setEmail("teste@email.com");
        dto.setCpf("12345678900");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(false);
        when(motoristaRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThrows(RecursoJaExistenteException.class, () -> motoristaService.cadastrarMotorista(dto));
    }

    @Test
    void deveLancarExcecaoSePlacaJaExistir() {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setEmail("teste@email.com");
        dto.setCpf("12345678900");
        dto.setPlacaVeiculo("ABC1234");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(false);
        when(motoristaRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(caminhaoRepository.existsByPlacaVeiculo(dto.getPlacaVeiculo())).thenReturn(true);

        assertThrows(RecursoJaExistenteException.class, () -> motoristaService.cadastrarMotorista(dto));
    }

    @Test
    void deveCadastrarMotoristaComSucesso() {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setEmail("teste@email.com");
        dto.setSenha("senha123");
        dto.setCpf("12345678900");
        dto.setPlacaVeiculo("ABC1234");
        dto.setNome_completo("João Motorista");

        when(usuarioService.emailJaCadastrado(dto.getEmail())).thenReturn(false);
        when(motoristaRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(caminhaoRepository.existsByPlacaVeiculo(dto.getPlacaVeiculo())).thenReturn(false);

        TipoUsuario tipoUsuario = new TipoUsuario();
        when(tipoUsuarioRepository.findByNome("MOTORISTA")).thenReturn(Optional.of(tipoUsuario));

        when(arquivoService.salvarImagem(any(MultipartFile.class))).thenReturn("foto.jpg");

        motoristaService.cadastrarMotorista(dto);

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(usuarioService, times(1)).cadastrarUsuario(any(Usuario.class));
        verify(caminhaoRepository, times(1)).save(any(Caminhao.class));
        verify(motoristaRepository, times(1)).save(any(Motorista.class));
    }

    @Test
    void deveBuscarMotoristaLogado() {
        Motorista resultado = motoristaService.buscarMotoristaLogado();
        assertNotNull(resultado);
        assertTrue(resultado.isValido());
    }

    @Test
    void deveAceitarFrete() {
        // Mock do frete existente
        Frete frete = new Frete();
        FreteStatus statusPendente = new FreteStatus();
        statusPendente.setNome("PENDENTE");
        frete.setFreteStatus(statusPendente);

        when(freteRepository.findById(1L)).thenReturn(Optional.of(frete));

        // Mock status ACEITO
        FreteStatus statusAceito = new FreteStatus();
        statusAceito.setNome("ACEITO");
        when(freteStatusRepository.findByNome("ACEITO")).thenReturn(Optional.of(statusAceito));

        motoristaService.aceitarFrete(1L);

        assertNotNull(frete.getMotorista());
        assertEquals("ACEITO", frete.getFreteStatus().getNome());
        assertEquals(LocalDate.now(), frete.getDataAtualizacao());
        verify(freteRepository, times(1)).save(frete);
    }

    @Test
    void deveListarTodosMotoristas() {
        motoristaService.listarTodos();
        verify(motoristaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarMotoristaPorId() {
        Motorista motorista = new Motorista();
        when(motoristaRepository.findById(1L)).thenReturn(Optional.of(motorista));

        Motorista resultado = motoristaService.buscarPorId(1L);

        assertEquals(motorista, resultado);
    }
}
