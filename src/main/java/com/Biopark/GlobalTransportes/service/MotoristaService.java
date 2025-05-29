package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.dto.CadastroMotoristaDto;
import com.Biopark.GlobalTransportes.exception.RecursoJaExistenteException;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository motoristaRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private CaminhaoRepository caminhaoRepository;
    @Autowired
    private ArquivoService arquivoService;

    public void cadastrarMotorista(CadastroMotoristaDto dto) {

        // Validações de negócio
        if (usuarioService.emailJaCadastrado(dto.getEmail())) {
            throw new RecursoJaExistenteException("Email já cadastrado.");
        }

        if (motoristaRepository.existsByCpf(dto.getCpf())) {
            throw new RecursoJaExistenteException("CPF já cadastrado.");
        }

        if (caminhaoRepository.existsByPlacaVeiculo(dto.getPlacaVeiculo())) {
            throw new RecursoJaExistenteException("Placa do veículo já cadastrada.");
        }

        // Salvar endereço
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

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        TipoUsuario tipo = tipoUsuarioRepository.findByNome("MOTORISTA")
                .orElseThrow(() -> new RuntimeException("Tipo MOTORISTA não encontrado"));

        usuario.setTipo(tipo);
        usuarioService.cadastrarUsuario(usuario); // já criptografa a senha


        // Salvar imagens e obter os nomes dos arquivos
        String nomeFotoFrente = arquivoService.salvarImagem(dto.getFotoFrente());
        String nomeFotoPlaca = arquivoService.salvarImagem(dto.getFotoPlaca());
        String nomeFotoCnh = arquivoService.salvarImagem(dto.getFotoCnh());

        // Criar caminhão
        Caminhao caminhao = new Caminhao();
        caminhao.setNumeroCrlv(dto.getNumeroCrlv());
        caminhao.setPlacaVeiculo(dto.getPlacaVeiculo());
        caminhao.setAno(dto.getAno());
        caminhao.setFabricante(dto.getFabricante());
        caminhao.setModelo(dto.getModelo());
        caminhao.setCor(dto.getCor());
        caminhao.setQuantidadeEixo(dto.getQuantidadeEixo());
        caminhao.setFoto_frente(nomeFotoFrente);
        caminhao.setFoto_placa(nomeFotoPlaca);
        caminhaoRepository.save(caminhao);

        // Criar motorista
        Motorista motorista = new Motorista();
        motorista.setNome_completo(dto.getNome_completo());
        motorista.setCpf(dto.getCpf());
        motorista.setNumero_cnh(dto.getNumero_cnh());
        motorista.setNumero_antt(dto.getNumero_antt());
        motorista.setTelefone_pessoal(dto.getTelefone_pessoal());
        motorista.setTelefone_seguranca_1(dto.getTelefone_seguranca_1());
        motorista.setTelefone_seguranca_2(dto.getTelefone_seguranca_2());
        motorista.setTelefone_seguranca_3(dto.getTelefone_seguranca_3());
        motorista.setTelefone_referencia_1(dto.getTelefone_referencia_1());
        motorista.setTelefone_referencia_2(dto.getTelefone_referencia_2());
        motorista.setTelefone_referencia_3(dto.getTelefone_referencia_3());
        motorista.setEmail_comercial(dto.getEmail_comercial());
        motorista.setFoto_cnh(nomeFotoCnh);
        motorista.setEndereco(endereco);
        motorista.setCaminhao(caminhao);
        motorista.setUsuario(usuario);
        motoristaRepository.save(motorista);
    }
}
