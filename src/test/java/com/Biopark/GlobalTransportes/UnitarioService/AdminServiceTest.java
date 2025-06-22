package com.Biopark.GlobalTransportes.UnitarioService;

import com.Biopark.GlobalTransportes.model.FreteStatus;
import com.Biopark.GlobalTransportes.repository.*;
import com.Biopark.GlobalTransportes.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MotoristaRepository motoristaRepository;

    @Mock
    private CaminhaoRepository caminhaoRepository;

    @Mock
    private FreteRepository freteRepository;

    @Mock
    private FreteStatusRepository freteStatusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTotalClientes() {
        when(clienteRepository.count()).thenReturn(10L);

        long total = adminService.getTotalClientes();

        assertEquals(10L, total);
        verify(clienteRepository, times(1)).count();
    }

    @Test
    void deveRetornarClientesPendentesValidacao() {
        when(clienteRepository.countByValidoFalse()).thenReturn(3L);

        long pendentes = adminService.getClientesPendentesValidacao();

        assertEquals(3L, pendentes);
        verify(clienteRepository, times(1)).countByValidoFalse();
    }

    @Test
    void deveRetornarTotalMotoristas() {
        when(motoristaRepository.count()).thenReturn(5L);

        long total = adminService.getTotalMotoristas();

        assertEquals(5L, total);
        verify(motoristaRepository, times(1)).count();
    }

    @Test
    void deveRetornarMotoristasPendentesValidacao() {
        when(motoristaRepository.countByValidoFalse()).thenReturn(2L);

        long pendentes = adminService.getMotoristasPendentesValidacao();

        assertEquals(2L, pendentes);
        verify(motoristaRepository, times(1)).countByValidoFalse();
    }

    @Test
    void deveRetornarTotalCaminhoes() {
        when(caminhaoRepository.count()).thenReturn(7L);

        long total = adminService.getTotalCaminhoes();

        assertEquals(7L, total);
        verify(caminhaoRepository, times(1)).count();
    }

    @Test
    void deveRetornarTotalFretes() {
        when(freteRepository.count()).thenReturn(20L);

        long total = adminService.getTotalFretes();

        assertEquals(20L, total);
        verify(freteRepository, times(1)).count();
    }

    @Test
    void deveRetornarFretesPorStatus() {
        FreteStatus status1 = new FreteStatus();
        status1.setNome("PENDENTE");

        FreteStatus status2 = new FreteStatus();
        status2.setNome("EM TRANSITO");

        List<FreteStatus> statusList = Arrays.asList(status1, status2);

        when(freteStatusRepository.findAll()).thenReturn(statusList);
        when(freteRepository.countByFreteStatus(status1)).thenReturn(5L);
        when(freteRepository.countByFreteStatus(status2)).thenReturn(10L);

        Map<String, Long> resultado = adminService.getFretesPorStatus();

        assertEquals(2, resultado.size());
        assertEquals(5L, resultado.get("PENDENTE"));
        assertEquals(10L, resultado.get("EM TRANSITO"));
    }

    @Test
    void deveRetornarFretesCriadosNoMesAtual() {
        when(freteRepository.countFretesCriadosNoMesAtual()).thenReturn(4L);

        long total = adminService.getFretesCriadosNoMesAtual();

        assertEquals(4L, total);
        verify(freteRepository, times(1)).countFretesCriadosNoMesAtual();
    }

    @Test
    void deveRetornarValorTotalMovimentado() {
        when(freteRepository.getValorTotalMovimentado()).thenReturn(15000.0);

        double total = adminService.getValorTotalMovimentado();

        assertEquals(15000.0, total);
        verify(freteRepository, times(1)).getValorTotalMovimentado();
    }

    @Test
    void deveRetornarZeroQuandoValorTotalMovimentadoForNull() {
        when(freteRepository.getValorTotalMovimentado()).thenReturn(null);

        double total = adminService.getValorTotalMovimentado();

        assertEquals(0.0, total);
    }

    @Test
    void deveRetornarTopMotoristasPorFretes() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"Motorista1", 10L},
                new Object[]{"Motorista2", 8L}
        );

        when(freteRepository.getTopMotoristasPorFretes()).thenReturn(mockResult);

        List<Object[]> resultado = adminService.getTopMotoristasPorFretes();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarTopClientesPorFretes() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{"Cliente1", 12L});

        when(freteRepository.getTopClientesPorFretes()).thenReturn(mockResult);

        List<Object[]> resultado = adminService.getTopClientesPorFretes();

        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarTopCidadesOrigem() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{"CidadeOrigem", 6L});


        when(freteRepository.getTopCidadesOrigem()).thenReturn(mockResult);

        List<Object[]> resultado = adminService.getTopCidadesOrigem();

        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarTopCidadesDestino() {

        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{"CidadeDestino", 7L});

        when(freteRepository.getTopCidadesDestino()).thenReturn(mockResult);

        List<Object[]> resultado = adminService.getTopCidadesDestino();

        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarFretesEmAndamentoPorEstado() {

        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{"SP", 3L});

        when(freteRepository.getFretesEmAndamentoPorEstado()).thenReturn(mockResult);

        List<Object[]> resultado = adminService.getFretesEmAndamentoPorEstado();

        assertEquals(1, resultado.size());
    }
}
