package com.Biopark.GlobalTransportes.UnitarioService;

import com.Biopark.GlobalTransportes.dto.FreteCadastroDTO;
import com.Biopark.GlobalTransportes.dto.FreteCheckpointDTO;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.*;
import com.Biopark.GlobalTransportes.service.FreteService;
import com.Biopark.GlobalTransportes.dto.EnderecoFreteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreteServiceTest {

    @InjectMocks
    private FreteService freteService;

    @Mock
    private FreteRepository freteRepository;

    @Mock
    private EnderecoFreteRepository enderecoFreteRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FreteStatusRepository freteStatusRepository;

    @Mock
    private MotoristaRepository motoristaRepository;

    @Mock
    private FreteCheckpointRepository freteCheckpointRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveExcluirFreteSeEstiverPendente() {
        Frete frete = new Frete();
        FreteStatus status = new FreteStatus();
        status.setNome("PENDENTE");
        frete.setFreteStatus(status);

        when(freteRepository.findById(1L)).thenReturn(Optional.of(frete));

        freteService.excluirFreteSePendente(1L);

        verify(freteRepository, times(1)).delete(frete);
    }

    @Test
    void deveLancarExcecaoAoExcluirFreteNaoPendente() {
        Frete frete = new Frete();
        FreteStatus status = new FreteStatus();
        status.setNome("EM TRANSITO");
        frete.setFreteStatus(status);

        when(freteRepository.findById(1L)).thenReturn(Optional.of(frete));

        assertThrows(RuntimeException.class, () -> freteService.excluirFreteSePendente(1L));
    }

    @Test
    void deveBuscarFretePorId() {
        Frete frete = new Frete();
        when(freteRepository.findById(1L)).thenReturn(Optional.of(frete));

        Frete resultado = freteService.buscarPorId(1L);

        assertEquals(frete, resultado);
    }

    @Test
    void deveListarTodosFretes() {
        freteService.listarTodos();
        verify(freteRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarFretesPorFiltro() {
        freteService.buscarFretesPorFiltro("carga");
        verify(freteRepository, times(1)).buscarPorFiltro("carga");
    }

    @Test
    void deveBuscarFretesDoClientePorFiltro() {
        freteService.buscarFretesDoClientePorFiltro(1L, "carga");
        verify(freteRepository, times(1)).buscarFretesDoClientePorFiltro(1L, "carga");
    }

    @Test
    void deveBuscarFretesDoMotoristaPorFiltro() {
        freteService.buscarFretesDoMotoristaPorFiltro(1L, "carga");
        verify(freteRepository, times(1)).buscarFretesDoMotoristaPorFiltro(1L, "carga");
    }

    @Test
    void deveBuscarFretesPendentesPorFiltro() {
        freteService.buscarFretesPendentesPorFiltro("carga");
        verify(freteRepository, times(1)).buscarFretesPendentesPorFiltro("carga");
    }

    @Test
    void deveListarFretesDoClienteLogado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("cliente@email.com");
        SecurityContextHolder.setContext(securityContext);

        Cliente cliente = new Cliente();
        when(clienteRepository.findByUsuarioEmail("cliente@email.com")).thenReturn(Optional.of(cliente));

        freteService.listarFretesDoClienteLogado();

        verify(freteRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void deveListarFretesDoMotoristaLogado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("motorista@email.com");
        SecurityContextHolder.setContext(securityContext);

        Motorista motorista = new Motorista();
        when(motoristaRepository.findByUsuarioEmail("motorista@email.com")).thenReturn(Optional.of(motorista));

        freteService.listarFretesDoMotoristaLogado();

        verify(freteRepository, times(1)).findByMotorista(motorista);
    }

    @Test
    void deveCadastrarCheckpoint() {
        FreteCheckpointDTO dto = new FreteCheckpointDTO();
        dto.setFreteId(1L);
        dto.setFreteStatusId(2L);
        dto.setCidade("Cidade");
        dto.setEstado("Estado");
        dto.setObservacoes("Obs");

        Frete frete = new Frete();
        FreteStatus status = new FreteStatus();
        status.setNome("EM TRANSITO");

        when(freteRepository.findById(1L)).thenReturn(Optional.of(frete));
        when(freteStatusRepository.findById(2L)).thenReturn(Optional.of(status));

        freteService.cadastrarCheckpoint(dto);

        verify(freteRepository, times(1)).save(frete);
        verify(freteCheckpointRepository, times(1)).save(any(FreteCheckpoint.class));
    }

    @Test
    void deveCadastrarFrete() {
        FreteCadastroDTO dto = new FreteCadastroDTO();
        dto.setTipoCarga("Eletrônicos");
        dto.setValorFrete(BigDecimal.valueOf(1000.0));
        dto.setPeso(500.0);
        dto.setComprimento(2.0);
        dto.setLargura(1.5);
        dto.setAltura(1.2);
        dto.setValorNotaFiscal(1500.0);

        // Criar Endereço Origem
        EnderecoFreteDTO origem = new EnderecoFreteDTO();
        origem.setLogradouro("Rua A");
        origem.setNumero("123");
        origem.setComplemento("Apto 1");
        origem.setBairro("Centro");
        origem.setCidade("Cidade A");
        origem.setEstado("Estado A");
        origem.setCep("00000-000");
        origem.setPais("Brasil");
        dto.setEnderecoOrigem(origem);

        // Criar Endereço Destino
        EnderecoFreteDTO destino = new EnderecoFreteDTO();
        destino.setLogradouro("Rua B");
        destino.setNumero("456");
        destino.setComplemento("Casa");
        destino.setBairro("Bairro B");
        destino.setCidade("Cidade B");
        destino.setEstado("Estado B");
        destino.setCep("11111-111");
        destino.setPais("Brasil");
        dto.setEnderecoDestino(destino);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("cliente@email.com");
        SecurityContextHolder.setContext(securityContext);

        Cliente cliente = new Cliente();
        when(clienteRepository.findByUsuarioEmail("cliente@email.com")).thenReturn(Optional.of(cliente));

        FreteStatus status = new FreteStatus();
        status.setNome("PENDENTE");
        when(freteStatusRepository.findByNome("PENDENTE")).thenReturn(Optional.of(status));

        when(freteRepository.save(any(Frete.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Frete freteSalvo = freteService.cadastrarFrete(dto);

        assertNotNull(freteSalvo);
        verify(enderecoFreteRepository, times(2)).save(any(EnderecoFrete.class));  // Origem e destino
        verify(freteRepository, times(1)).save(any(Frete.class));
    }

}
