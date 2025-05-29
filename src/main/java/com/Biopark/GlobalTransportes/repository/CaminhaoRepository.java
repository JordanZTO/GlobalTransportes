package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    boolean existsByPlacaVeiculo(String placaVeiculo);
}
