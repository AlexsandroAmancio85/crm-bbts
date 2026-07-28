package br.com.bbts.crm.oportunidade.repository;

import br.com.bbts.crm.oportunidade.entity.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    List<Oportunidade> findByClienteId(Long clienteId);
}
