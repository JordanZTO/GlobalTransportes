// MotoristaRepository.java
package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    boolean existsByCpf(String cpf);
}
