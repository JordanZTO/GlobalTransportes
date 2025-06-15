package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.model.FreteStatus;
import com.Biopark.GlobalTransportes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private CaminhaoRepository caminhaoRepository;

    @Autowired
    private FreteRepository freteRepository;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    public long getTotalClientes() {
        return clienteRepository.count();
    }

    public long getClientesPendentesValidacao() {
        return clienteRepository.countByValidoFalse();
    }

    public long getTotalMotoristas() {
        return motoristaRepository.count();
    }

    public long getMotoristasPendentesValidacao() {
        return motoristaRepository.countByValidoFalse();
    }

    public long getTotalCaminhoes() {
        return caminhaoRepository.count();
    }

    public long getTotalFretes() {
        return freteRepository.count();
    }

    public Map<String, Long> getFretesPorStatus() {
        Map<String, Long> statusMap = new HashMap<>();
        freteStatusRepository.findAll().forEach(status -> {
            Long count = freteRepository.countByFreteStatus(status);
            statusMap.put(status.getNome(), count);
        });
        return statusMap;
    }

    public long getFretesCriadosNoMesAtual() {
        return freteRepository.countFretesCriadosNoMesAtual();
    }

    public Double getValorTotalMovimentado() {
        Double total = freteRepository.getValorTotalMovimentado();
        return total != null ? total : 0.0;
    }

    public List<Object[]> getTopMotoristasPorFretes() {
        return freteRepository.getTopMotoristasPorFretes().stream().limit(5).toList();
    }

    public List<Object[]> getTopClientesPorFretes() {
        return freteRepository.getTopClientesPorFretes().stream().limit(5).toList();
    }

    public List<Object[]> getTopCidadesOrigem() {
        return freteRepository.getTopCidadesOrigem().stream().limit(5).toList();
    }

    public List<Object[]> getTopCidadesDestino() {
        return freteRepository.getTopCidadesDestino().stream().limit(5).toList();
    }

    public List<Object[]> getFretesEmAndamentoPorEstado() {
        return freteRepository.getFretesEmAndamentoPorEstado();
    }
}
