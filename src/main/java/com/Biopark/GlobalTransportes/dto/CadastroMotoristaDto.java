package com.Biopark.GlobalTransportes.dto;

import org.springframework.web.multipart.MultipartFile;

public class CadastroMotoristaDto {

    //Motorista
    private String nome_completo;
    private String cpf;
    private String numero_cnh;
    private String numero_antt;
    private String telefone_pessoal;
    private String telefone_seguranca_1;
    private String telefone_seguranca_2;
    private String telefone_seguranca_3;
    private String telefone_referencia_1;
    private String telefone_referencia_2;
    private String telefone_referencia_3;
    private String email_comercial;
    private MultipartFile fotoCnh;

    // Endereço
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;

    // Caminhão
    private String numeroCrlv;
    private String placaVeiculo;
    private int ano;
    private String fabricante;
    private String modelo;
    private String cor;
    private int quantidadeEixo;
    private MultipartFile fotoFrente;
    private MultipartFile fotoPlaca;

    // Usuário
    private String email;
    private String senha;

    public CadastroMotoristaDto() {}

    // Getters e Setters


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

    public MultipartFile getFotoCnh() {
        return fotoCnh;
    }

    public void setFotoCnh(MultipartFile fotoCnh) {
        this.fotoCnh = fotoCnh;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNumeroCrlv() {
        return numeroCrlv;
    }

    public void setNumeroCrlv(String numeroCrlv) {
        this.numeroCrlv = numeroCrlv;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getQuantidadeEixo() {
        return quantidadeEixo;
    }

    public void setQuantidadeEixo(int quantidadeEixo) {
        this.quantidadeEixo = quantidadeEixo;
    }

    public MultipartFile getFotoFrente() {
        return fotoFrente;
    }

    public void setFotoFrente(MultipartFile fotoFrente) {
        this.fotoFrente = fotoFrente;
    }

    public MultipartFile getFotoPlaca() {
        return fotoPlaca;
    }

    public void setFotoPlaca(MultipartFile fotoPlaca) {
        this.fotoPlaca = fotoPlaca;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
