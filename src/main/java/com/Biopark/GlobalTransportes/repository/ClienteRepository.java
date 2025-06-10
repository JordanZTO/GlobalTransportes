package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuarioEmail(String email);
    Optional<Cliente> findByUsuario(Usuario usuario);
}
