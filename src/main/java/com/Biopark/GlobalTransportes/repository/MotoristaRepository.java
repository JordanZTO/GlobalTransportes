// MotoristaRepository.java
package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Motorista;
import com.Biopark.GlobalTransportes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    boolean existsByCpf(String cpf);
    Optional<Motorista> findByUsuario(Usuario usuario);
    Optional<Motorista> findByUsuarioEmail(String email);

}
