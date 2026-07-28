package br.com.bbts.crm.logsistema.repository;

import br.com.bbts.crm.logsistema.entity.LogSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogSistemaRepository extends JpaRepository<LogSistema, Long> {
    List<LogSistema> findByUsuarioIdOrderByDataHoraDesc(Long usuarioId);
    List<LogSistema> findTop100ByOrderByDataHoraDesc();
}
