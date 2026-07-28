package br.com.bbts.crm.agenda.repository;

import br.com.bbts.crm.agenda.entity.RetornoAgendado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetornoAgendadoRepository extends JpaRepository<RetornoAgendado, Long> {
    List<RetornoAgendado> findByVendedorIdOrderByDataAgendadaAsc(Long vendedorId);
    List<RetornoAgendado> findAllByOrderByDataAgendadaAsc();
}
