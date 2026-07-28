package br.com.bbts.crm.importacao.repository;

import br.com.bbts.crm.importacao.entity.LogImportacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogImportacaoRepository extends JpaRepository<LogImportacao, Long> {
    List<LogImportacao> findByArquivoImportadoId(Long arquivoImportadoId);
}
