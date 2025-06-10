package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "frete_checkpoint")
public class FreteCheckpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkpointId;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Lob
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "checkpoint_status_id", nullable = false)
    private CheckpointStatus checkpointStatus;

    @ManyToOne
    @JoinColumn(name = "frete_id", nullable = false)
    private Frete frete;

    // Getters e Setters

    public Long getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Long checkpointId) {
        this.checkpointId = checkpointId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public CheckpointStatus getCheckpointStatus() {
        return checkpointStatus;
    }

    public void setCheckpointStatus(CheckpointStatus checkpointStatus) {
        this.checkpointStatus = checkpointStatus;
    }

    public Frete getFrete() {
        return frete;
    }

    public void setFrete(Frete frete) {
        this.frete = frete;
    }
}