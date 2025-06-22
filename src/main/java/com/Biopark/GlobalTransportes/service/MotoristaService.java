package com.Biopark.GlobalTransportes.service;

import com.Biopark.GlobalTransportes.dto.MotoristaDTO;
import com.Biopark.GlobalTransportes.exception.RecursoJaExistenteException;
import com.Biopark.GlobalTransportes.model.*;
import com.Biopark.GlobalTransportes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    FreteRepository freteRepository;
    @Autowired
    FreteStatusRepository freteStatusRepository;

    public void salvar(Motorista motorista) {
        motoristaRepository.save(motorista);
    }

    public void cadastrarMotorista(MotoristaDTO dto) {

        System.out.println("Iniciando cadastro de motorista...");

        if (usuarioService.emailJaCadastrado(dto.getEmail())) {
            throw new RecursoJaExistenteException("Email já cadastrado.");
        }

        if (motoristaRepository.existsByCpf(dto.getCpf())) {
            throw new RecursoJaExistenteException("CPF já cadastrado.");
        }

        if (caminhaoRepository.existsByPlacaVeiculo(dto.getPlacaVeiculo())) {
            throw new RecursoJaExistenteException("Placa do veículo já cadastrada.");
        }

        System.out.println("Validações básicas passaram. Criando endereço...");

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

        System.out.println("Endereço salvo. Criando usuário...");

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        TipoUsuario tipo = tipoUsuarioRepository.findByNome("MOTORISTA")
                .orElseThrow(() -> new RuntimeException("Tipo MOTORISTA não encontrado"));

        usuario.setTipo(tipo);
        usuarioService.cadastrarUsuario(usuario);

        System.out.println("Usuário criado. Salvando imagens...");

        String nomeFotoFrente = arquivoService.salvarImagem(dto.getFotoFrente());
        String nomeFotoPlaca = arquivoService.salvarImagem(dto.getFotoPlaca());
        String nomeFotoCnh = arquivoService.salvarImagem(dto.getFotoCnh());

        System.out.println("Imagens salvas - Frente: " + nomeFotoFrente + ", Placa: " + nomeFotoPlaca + ", CNH: " + nomeFotoCnh);

        System.out.println("Criando caminhão...");

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

        System.out.println("Caminhão salvo. Criando motorista...");

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

        System.out.println("Motorista cadastrado com sucesso!");
    }

    public void aceitarFrete(Long freteId) {
        Frete frete = freteRepository.findById(freteId)
                .orElseThrow(() -> new RuntimeException("Frete não encontrado"));

        if (!"PENDENTE".equalsIgnoreCase(frete.getFreteStatus().getNome())) {
            throw new RuntimeException("Este frete não está disponível para aceitação.");
        }

        Motorista motorista = buscarMotoristaLogado();

        if (!motorista.isValido()) {
            throw new RuntimeException("Motorista não validado. Não é possível aceitar frete.");
        }

        FreteStatus statusAceito = freteStatusRepository.findByNome("ACEITO")
                .orElseThrow(() -> new RuntimeException("Status 'ACEITO' não encontrado"));

        frete.setMotorista(motorista);
        frete.setFreteStatus(statusAceito);
        frete.setDataAtualizacao(LocalDate.now());

        freteRepository.save(frete);
    }


    public Motorista buscarMotoristaLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return motoristaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado para o usuário logado"));
    }

    public MotoristaDTO obterDadosParaEdicao(Motorista motorista) {
        MotoristaDTO dto = new MotoristaDTO();

        dto.setMotorista_id(motorista.getMotorista_id());
        dto.setNome_completo(motorista.getNome_completo());
        dto.setCpf(motorista.getCpf());
        dto.setNumero_cnh(motorista.getNumero_cnh());
        dto.setNumero_antt(motorista.getNumero_antt());
        dto.setTelefone_pessoal(motorista.getTelefone_pessoal());
        dto.setTelefone_seguranca_1(motorista.getTelefone_seguranca_1());
        dto.setTelefone_seguranca_2(motorista.getTelefone_seguranca_2());
        dto.setTelefone_seguranca_3(motorista.getTelefone_seguranca_3());
        dto.setTelefone_referencia_1(motorista.getTelefone_referencia_1());
        dto.setTelefone_referencia_2(motorista.getTelefone_referencia_2());
        dto.setTelefone_referencia_3(motorista.getTelefone_referencia_3());
        dto.setEmail_comercial(motorista.getEmail_comercial());
        dto.setValido(motorista.isValido());

        Endereco endereco = motorista.getEndereco();
        if (endereco != null) {
            dto.setLogradouro(endereco.getLogradouro());
            dto.setNumero(endereco.getNumero());
            dto.setComplemento(endereco.getComplemento());
            dto.setBairro(endereco.getBairro());
            dto.setCidade(endereco.getCidade());
            dto.setEstado(endereco.getEstado());
            dto.setCep(endereco.getCep());
            dto.setPais(endereco.getPais());
        }

        Caminhao caminhao = motorista.getCaminhao();
        if (caminhao != null) {
            dto.setNumeroCrlv(caminhao.getNumeroCrlv());
            dto.setPlacaVeiculo(caminhao.getPlacaVeiculo());
            dto.setAno(caminhao.getAno());
            dto.setFabricante(caminhao.getFabricante());
            dto.setModelo(caminhao.getModelo());
            dto.setCor(caminhao.getCor());
            dto.setQuantidadeEixo(caminhao.getQuantidadeEixo());
        }

        dto.setFotoFrenteNome(motorista.getCaminhao().getFoto_frente());
        dto.setFotoPlacaNome(motorista.getCaminhao().getFoto_placa());
        dto.setFotoCnhNome(motorista.getFoto_cnh());

        return dto;
    }

    public void atualizarPerfil(MotoristaDTO dto) {
        Motorista motorista = buscarMotoristaLogado();

        motorista.setNome_completo(dto.getNome_completo());
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

        if (dto.getFotoCnh() != null && !dto.getFotoCnh().isEmpty()) {
            String nomeFotoCnh = arquivoService.salvarImagem(dto.getFotoCnh());
            motorista.setFoto_cnh(nomeFotoCnh);
        }

        if (dto.isRemoverFotoCnh()) {
            arquivoService.excluirImagem(motorista.getFoto_cnh());
            motorista.setFoto_cnh(null);
        }

        Endereco endereco = motorista.getEndereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        endereco.setPais(dto.getPais());
        enderecoRepository.save(endereco);

        Caminhao caminhao = motorista.getCaminhao();
        caminhao.setNumeroCrlv(dto.getNumeroCrlv());
        caminhao.setPlacaVeiculo(dto.getPlacaVeiculo());
        caminhao.setAno(dto.getAno());
        caminhao.setFabricante(dto.getFabricante());
        caminhao.setModelo(dto.getModelo());
        caminhao.setCor(dto.getCor());
        caminhao.setQuantidadeEixo(dto.getQuantidadeEixo());

        if (dto.getFotoFrente() != null && !dto.getFotoFrente().isEmpty()) {
            String nomeFotoFrente = arquivoService.salvarImagem(dto.getFotoFrente());
            caminhao.setFoto_frente(nomeFotoFrente);
        }

        if (dto.getFotoPlaca() != null && !dto.getFotoPlaca().isEmpty()) {
            String nomeFotoPlaca = arquivoService.salvarImagem(dto.getFotoPlaca());
            caminhao.setFoto_placa(nomeFotoPlaca);
        }

        if (dto.isRemoverFotoPlaca()) {
            arquivoService.excluirImagem(motorista.getCaminhao().getFoto_placa());
            motorista.getCaminhao().setFoto_placa(null);
        }

        if (dto.isRemoverFotoCnh()) {
            arquivoService.excluirImagem(motorista.getFoto_cnh());
            motorista.setFoto_cnh(null);
        }

        caminhaoRepository.save(caminhao);
        motoristaRepository.save(motorista);
    }

    public List<Motorista> listarTodos() {
        return motoristaRepository.findAll();
    }

    public Motorista buscarPorId(Long id) {
        return motoristaRepository.findById(id)
                .orElse(null);
    }

}
