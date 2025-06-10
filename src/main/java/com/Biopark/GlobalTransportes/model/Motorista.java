package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "motorista")
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long motorista_id;

    @Column(nullable = false)
    private String nome_completo;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String numero_cnh;

    @Column(nullable = false)
    private String numero_antt;

    @Column(nullable = false)
    private String telefone_pessoal;

    private String telefone_seguranca_1;
    private String telefone_seguranca_2;
    private String telefone_seguranca_3;

    private String telefone_referencia_1;
    private String telefone_referencia_2;
    private String telefone_referencia_3;

    @Column(nullable = false)
    private String email_comercial;

    private String foto_cnh;

    @Transient
    private MultipartFile arquivoFotoCnh;

    @Column(nullable = false)
    private boolean valido;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "caminhao_id")
    private Caminhao caminhao;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    public Motorista() {}

    //Getters e Setters
    public Long getMotorista_id() {
        return motorista_id;
    }

    public void setMotorista_id(Long motorista_id) {
        this.motorista_id = motorista_id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNumero_cnh() {
        return numero_cnh;
    }

    public void setNumero_cnh(String numero_cnh) {
        this.numero_cnh = numero_cnh;
    }

    public String getNumero_antt() {
        return numero_antt;
    }

    public void setNumero_antt(String numero_antt) {
        this.numero_antt = numero_antt;
    }

    public String getTelefone_pessoal() {
        return telefone_pessoal;
    }

    public void setTelefone_pessoal(String telefone_pessoal) {
        this.telefone_pessoal = telefone_pessoal;
    }

    public String getTelefone_seguranca_1() {
        return telefone_seguranca_1;
    }

    public void setTelefone_seguranca_1(String telefone_seguranca_1) {
        this.telefone_seguranca_1 = telefone_seguranca_1;
    }

    public String getTelefone_seguranca_2() {
        return telefone_seguranca_2;
    }

    public void setTelefone_seguranca_2(String telefone_seguranca_2) {
        this.telefone_seguranca_2 = telefone_seguranca_2;
    }

    public String getTelefone_seguranca_3() {
        return telefone_seguranca_3;
    }

    public void setTelefone_seguranca_3(String telefone_seguranca_3) {
        this.telefone_seguranca_3 = telefone_seguranca_3;
    }

    public String getTelefone_referencia_1() {
        return telefone_referencia_1;
    }

    public void setTelefone_referencia_1(String telefone_referencia_1) {
        this.telefone_referencia_1 = telefone_referencia_1;
    }

    public String getTelefone_referencia_2() {
        return telefone_referencia_2;
    }

    public void setTelefone_referencia_2(String telefone_referencia_2) {
        this.telefone_referencia_2 = telefone_referencia_2;
    }

    public String getTelefone_referencia_3() {
        return telefone_referencia_3;
    }

    public void setTelefone_referencia_3(String telefone_referencia_3) {
        this.telefone_referencia_3 = telefone_referencia_3;
    }

    public String getEmail_comercial() {
        return email_comercial;
    }

    public void setEmail_comercial(String email_comercial) {
        this.email_comercial = email_comercial;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public void setCaminhao(Caminhao caminhao) {
        this.caminhao = caminhao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFoto_cnh() {
        return foto_cnh;
    }

    public void setFoto_cnh(String foto_cnh) {
        this.foto_cnh = foto_cnh;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public MultipartFile getArquivoFotoCnh() {
        return arquivoFotoCnh;
    }

    public void setArquivoFotoCnh(MultipartFile arquivoFotoCnh) {
        this.arquivoFotoCnh = arquivoFotoCnh;
    }
}
