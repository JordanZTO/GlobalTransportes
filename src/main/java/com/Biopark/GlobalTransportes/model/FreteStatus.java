package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "frete_status")
public class FreteStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freteStatusId;

    @Column(nullable = false, unique = true, length = 20)
    private String nome;

    // Getters e Setters

    public Long getFreteStatusId() {
        return freteStatusId;
    }

    public void setFreteStatusId(Long freteStatusId) {
        this.freteStatusId = freteStatusId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}