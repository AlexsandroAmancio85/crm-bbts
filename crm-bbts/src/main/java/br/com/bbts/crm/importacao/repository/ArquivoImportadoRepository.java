package br.com.bbts.crm.importacao.repository;

import br.com.bbts.crm.importacao.entity.ArquivoImportado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoImportadoRepository extends JpaRepository<ArquivoImportado, Long> {
}
