package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.dto.FreteCadastroDTO;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDate;

@Service
public class FreteService {

    @Autowired
    private FreteRepository freteRepository;

    @Autowired
    private EnderecoFreteRepository enderecoFreteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FreteStatusRepository freteStatusRepository;

    public void excluirFreteSePendente(Long id) {
        Frete frete = freteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frete não encontrado com ID: " + id));

        if (!"PENDENTE".equalsIgnoreCase(frete.getFreteStatus().getNome())) {
            throw new RuntimeException("Frete não pode ser excluído. Status atual: " + frete.getFreteStatus().getNome());
        }

        freteRepository.delete(frete);
    }


    public Frete buscarPorId(Long id) {
        return freteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frete não encontrado"));
    }


    public List<Frete> listarFretesDoClienteLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email do usuário logado

        Cliente cliente = clienteRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o usuário com e-mail: " + email));

        return freteRepository.findByCliente(cliente);
    }

    public Frete cadastrarFrete(FreteCadastroDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // e-mail do usuário logado

        Cliente cliente = clienteRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o usuário com e-mail: " + email));

        EnderecoFrete origem = new EnderecoFrete();
        origem.setLogradouro(dto.getEnderecoOrigem().getLogradouro());
        origem.setNumero(dto.getEnderecoOrigem().getNumero());
        origem.setComplemento(dto.getEnderecoOrigem().getComplemento());
        origem.setBairro(dto.getEnderecoOrigem().getBairro());
        origem.setCidade(dto.getEnderecoOrigem().getCidade());
        origem.setEstado(dto.getEnderecoOrigem().getEstado());
        origem.setCep(dto.getEnderecoOrigem().getCep());
        origem.setPais(dto.getEnderecoOrigem().getPais());
        enderecoFreteRepository.save(origem);

        EnderecoFrete destino = new EnderecoFrete();
        destino.setLogradouro(dto.getEnderecoDestino().getLogradouro());
        destino.setNumero(dto.getEnderecoDestino().getNumero());
        destino.setComplemento(dto.getEnderecoDestino().getComplemento());
        destino.setBairro(dto.getEnderecoDestino().getBairro());
        destino.setCidade(dto.getEnderecoDestino().getCidade());
        destino.setEstado(dto.getEnderecoDestino().getEstado());
        destino.setCep(dto.getEnderecoDestino().getCep());
        destino.setPais(dto.getEnderecoDestino().getPais());
        enderecoFreteRepository.save(destino);

        FreteStatus statusPendente = freteStatusRepository.findByNome("PENDENTE")
                .orElseThrow(() -> new RuntimeException("Status 'PENDENTE' não encontrado"));

        Frete frete = new Frete();
        frete.setTipoCarga(dto.getTipoCarga());
        frete.setValorFrete(dto.getValorFrete());
        frete.setPeso(dto.getPeso());
        frete.setComprimento(dto.getComprimento());
        frete.setLargura(dto.getLargura());
        frete.setAltura(dto.getAltura());
        frete.setValorNotaFiscal(dto.getValorNotaFiscal());
        frete.setDataCriacao(LocalDate.now());
        frete.setDataAtualizacao(LocalDate.now());
        frete.setEnderecoOrigem(origem);
        frete.setEnderecoDestino(destino);
        frete.setCliente(cliente);
        frete.setFreteStatus(statusPendente);

        return freteRepository.save(frete);
    }

}
