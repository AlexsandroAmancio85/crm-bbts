package br.com.bbts.crm.logprodutividade.repository;

import br.com.bbts.crm.logprodutividade.entity.LogProdutividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LogProdutividadeRepository extends JpaRepository<LogProdutividade, Long> {
    List<LogProdutividade> findByVendedorIdOrderByDataHoraDesc(Long vendedorId);
    List<LogProdutividade> findByClienteIdOrderByDataHoraDesc(Long clienteId);

    @Query("SELECT l FROM LogProdutividade l WHERE l.vendedor.id = :vendedorId " +
           "AND l.dataHora BETWEEN :inicio AND :fim ORDER BY l.dataHora DESC")
    List<LogProdutividade> findByVendedorAndPeriodo(Long vendedorId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT l.tipoAtividade, COUNT(l) FROM LogProdutividade l " +
           "WHERE l.vendedor.id = :vendedorId GROUP BY l.tipoAtividade")
    List<Object[]> contarPorTipoAtividade(Long vendedorId);
}
