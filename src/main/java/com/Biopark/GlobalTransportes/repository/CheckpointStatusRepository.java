package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.CheckpointStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckpointStatusRepository extends JpaRepository<CheckpointStatus, Long> {
}
