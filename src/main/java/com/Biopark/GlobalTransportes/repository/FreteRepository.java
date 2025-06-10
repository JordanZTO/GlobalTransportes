package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {
    List<Frete> findByCliente(Cliente cliente);
    List<Frete> findByFreteStatusNome(String nomeStatus);
    List<Frete> findByMotorista(Motorista motorista);

}
