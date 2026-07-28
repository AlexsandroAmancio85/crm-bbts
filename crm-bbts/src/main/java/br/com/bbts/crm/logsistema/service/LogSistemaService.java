package br.com.bbts.crm.logsistema.service;

import br.com.bbts.crm.logsistema.entity.LogSistema;
import br.com.bbts.crm.logsistema.repository.LogSistemaRepository;
import br.com.bbts.crm.usuario.entity.Usuario;
import br.com.bbts.crm.usuario.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogSistemaService {

    private final LogSistemaRepository logSistemaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String username, String acao, String entidade, Long entidadeId, String detalhes, String ip) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) return;
        logSistemaRepository.save(LogSistema.builder()
                .usuario(usuario).acao(acao).entidade(entidade)
                .entidadeId(entidadeId).detalhes(detalhes).ip(ip).build());
    }

    public List<LogSistema> listarRecentes() {
        return logSistemaRepository.findTop100ByOrderByDataHoraDesc();
    }

    public List<LogSistema> listarPorUsuario(Long usuarioId) {
        return logSistemaRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioId);
    }
}
