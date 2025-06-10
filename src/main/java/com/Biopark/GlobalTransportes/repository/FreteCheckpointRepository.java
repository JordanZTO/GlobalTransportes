package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.FreteCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FreteCheckpointRepository extends JpaRepository<FreteCheckpoint, Long> {
    List<FreteCheckpoint> findByFreteFreteIdOrderByDataHoraAsc(Long freteId);
}
