package com.Biopark.GlobalTransportes.repository;

import com.Biopark.GlobalTransportes.model.Cliente;
import com.Biopark.GlobalTransportes.model.Frete;
import com.Biopark.GlobalTransportes.model.FreteStatus;
import com.Biopark.GlobalTransportes.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {
    List<Frete> findByCliente(Cliente cliente);
    List<Frete> findByFreteStatusNome(String nomeStatus);
    List<Frete> findByMotorista(Motorista motorista);

    long countByFreteStatus(FreteStatus status);

    @Query("SELECT COUNT(f) FROM Frete f WHERE MONTH(f.dataCriacao) = MONTH(CURRENT_DATE) AND YEAR(f.dataCriacao) = YEAR(CURRENT_DATE)")
    long countFretesCriadosNoMesAtual();

    @Query("SELECT SUM(f.valorFrete) FROM Frete f")
    Double getValorTotalMovimentado();

    @Query("SELECT f.motorista.nome_completo, COUNT(f) FROM Frete f GROUP BY f.motorista.nome_completo ORDER BY COUNT(f) DESC")
    List<Object[]> getTopMotoristasPorFretes();

    @Query("SELECT f.cliente.nome, COUNT(f) FROM Frete f GROUP BY f.cliente.nome ORDER BY COUNT(f) DESC")
    List<Object[]> getTopClientesPorFretes();

    @Query("SELECT f.enderecoOrigem.cidade, COUNT(f) FROM Frete f GROUP BY f.enderecoOrigem.cidade ORDER BY COUNT(f) DESC")
    List<Object[]> getTopCidadesOrigem();

    @Query("SELECT f.enderecoDestino.cidade, COUNT(f) FROM Frete f GROUP BY f.enderecoDestino.cidade ORDER BY COUNT(f) DESC")
    List<Object[]> getTopCidadesDestino();

    @Query("SELECT f.enderecoDestino.estado, COUNT(f) FROM Frete f WHERE f.freteStatus.nome IN ('ACEITO', 'EM TRANSITO') GROUP BY f.enderecoDestino.estado")
    List<Object[]> getFretesEmAndamentoPorEstado();

    @Query("SELECT f FROM Frete f " +
            "WHERE LOWER(f.tipoCarga) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(f.enderecoOrigem.cidade) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(f.enderecoDestino.cidade) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(f.freteStatus.nome) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Frete> buscarPorFiltro(@Param("filtro") String filtro);


}
