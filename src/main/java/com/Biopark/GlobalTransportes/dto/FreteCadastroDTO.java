package com.Biopark.GlobalTransportes.dto;

import java.math.BigDecimal;

public class FreteCadastroDTO {
    private String tipoCarga;
    private BigDecimal valorFrete;
    private double peso;
    private double comprimento;
    private double largura;
    private double altura;
    private double valorNotaFiscal;
    private EnderecoFreteDTO enderecoOrigem;
    private EnderecoFreteDTO enderecoDestino;

    // Getters e Setters

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getValorNotaFiscal() {
        return valorNotaFiscal;
    }

    public void setValorNotaFiscal(double valorNotaFiscal) {
        this.valorNotaFiscal = valorNotaFiscal;
    }

    public EnderecoFreteDTO getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public void setEnderecoOrigem(EnderecoFreteDTO enderecoOrigem) {
        this.enderecoOrigem = enderecoOrigem;
    }

    public EnderecoFreteDTO getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(EnderecoFreteDTO enderecoDestino) {
        this.enderecoDestino = enderecoDestino;
    }
}