package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "checkpoint_status")
public class CheckpointStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkpointStatusId;

    @Column(nullable = false, unique = true, length = 20)
    private String nome;

    // Getters e Setters

    public Long getCheckpointStatusId() {
        return checkpointStatusId;
    }

    public void setCheckpointStatusId(Long checkpointStatusId) {
        this.checkpointStatusId = checkpointStatusId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
