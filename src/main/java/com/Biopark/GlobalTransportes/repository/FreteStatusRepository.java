package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.FreteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreteStatusRepository extends JpaRepository<FreteStatus, Long> {
    Optional<FreteStatus> findByNome(String nome);
}
