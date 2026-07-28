package br.com.bbts.crm.qualificacao.repository;

import br.com.bbts.crm.qualificacao.entity.QualificacaoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificacaoClienteRepository extends JpaRepository<QualificacaoCliente, Long> {
}
