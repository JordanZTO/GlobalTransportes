package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Byte> {
    Optional<TipoUsuario> findByNome (String nome);
}
