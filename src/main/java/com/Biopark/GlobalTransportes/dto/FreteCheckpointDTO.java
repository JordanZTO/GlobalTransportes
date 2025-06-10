package com.Biopark.GlobalTransportes.dto;

import java.time.LocalDateTime;

public class FreteCheckpointDTO {

    private LocalDateTime dataHora;
    private String estado;
    private String cidade;
    private String observacoes;
    private Long freteStatusId;
    private Long freteId;

    //Getters e Setters

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

    public Long getFreteStatusId() {
        return freteStatusId;
    }

    public void setFreteStatusId(Long freteStatusId) {
        this.freteStatusId = freteStatusId;
    }

    public Long getFreteId() {
        return freteId;
    }

    public void setFreteId(Long freteId) {
        this.freteId = freteId;
    }
}
