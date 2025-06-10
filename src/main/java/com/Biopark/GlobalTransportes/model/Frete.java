package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "frete")
public class Frete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freteId;

    @Column(nullable = false)
    private String tipoCarga;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFrete;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false)
    private double comprimento;

    @Column(nullable = false)
    private double largura;

    @Column(nullable = false)
    private double altura;

    @Column(nullable = false)
    private double valorNotaFiscal;

    @Column(nullable = false)
    private LocalDate dataCriacao;

    @Column(nullable = false)
    private LocalDate dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "frete_status_id")
    private FreteStatus freteStatus;

    @ManyToOne
    @JoinColumn(name = "endereco_origem_id", nullable = false)
    private EnderecoFrete enderecoOrigem;

    @ManyToOne
    @JoinColumn(name = "endereco_destino_id", nullable = false)
    private EnderecoFrete enderecoDestino;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "frete")
    private List<FreteCheckpoint> checkpoints;

    // Getters e Setters

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public Long getFreteId() {
        return freteId;
    }

    public void setFreteId(Long freteId) {
        this.freteId = freteId;
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public FreteStatus getFreteStatus() {
        return freteStatus;
    }

    public void setFreteStatus(FreteStatus freteStatus) {
        this.freteStatus = freteStatus;
    }

    public EnderecoFrete getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public void setEnderecoOrigem(EnderecoFrete enderecoOrigem) {
        this.enderecoOrigem = enderecoOrigem;
    }

    public EnderecoFrete getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(EnderecoFrete enderecoDestino) {
        this.enderecoDestino = enderecoDestino;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<FreteCheckpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<FreteCheckpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}