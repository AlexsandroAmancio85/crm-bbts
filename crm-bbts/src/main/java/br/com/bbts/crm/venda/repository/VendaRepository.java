package br.com.bbts.crm.venda.repository;

import br.com.bbts.crm.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
