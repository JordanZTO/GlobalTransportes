package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreteRepository extends JpaRepository<Frete, Long> {
    List<Frete> findByCliente(Cliente cliente);
}
