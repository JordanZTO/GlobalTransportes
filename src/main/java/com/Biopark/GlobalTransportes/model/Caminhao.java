package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "caminhao")
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caminhaoId;

    @Column(nullable = false)
    private String numeroCrlv;

    @Column(nullable = false)
    private String placaVeiculo;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private String fabricante;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String cor;

    @Column(nullable = false)
    private int quantidadeEixo;

    @Column(nullable = false)
    private String foto_frente;

    @Column(nullable = false)
    private String foto_placa;

    //Getters e Setters
    public Long getCaminhaoId() {
        return caminhaoId;
    }

    public void setCaminhaoId(Long caminhaoId) {
        this.caminhaoId = caminhaoId;
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

    public String getFoto_frente() {
        return foto_frente;
    }

    public void setFoto_frente(String foto_frente) {
        this.foto_frente = foto_frente;
    }

    public String getFoto_placa() {
        return foto_placa;
    }

    public void setFoto_placa(String foto_placa) {
        this.foto_placa = foto_placa;
    }
}