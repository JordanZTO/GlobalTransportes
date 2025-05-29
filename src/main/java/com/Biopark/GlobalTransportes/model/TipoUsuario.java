package com.Biopark.GlobalTransportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {

    @Id
    @Column(name = "tipo_usuario_id")
    private Byte id;

    @Column(nullable = false, unique = true)
    private String nome;

    // Getters e setters

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
