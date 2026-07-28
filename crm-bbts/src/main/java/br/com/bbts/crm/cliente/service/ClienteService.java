package br.com.bbts.crm.cliente.service;

import br.com.bbts.crm.cliente.dto.AtualizarAtendimentoRequest;
import br.com.bbts.crm.cliente.dto.AtualizarStatusRequest;
import br.com.bbts.crm.cliente.dto.ClienteFiltroDTO;
import br.com.bbts.crm.cliente.dto.ClienteRequest;
import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.PerfilUsuario;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.exception.BusinessException;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.logprodutividade.service.LogProdutividadeService;
import br.com.bbts.crm.logsistema.service.LogSistemaService;
import br.com.bbts.crm.usuario.entity.Usuario;
import br.com.bbts.crm.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final LogProdutividadeService logProdutividadeService;
    private final LogSistemaService logSistemaService;

    /** Lista clientes respeitando o perfil do usuário autenticado (item 7.2). */
    public List<Cliente> listar(ClienteFiltroDTO filtro) {
        Specification<Cliente> spec = Specification.where(null);
        Usuario usuarioAtual = obterUsuarioAtual();

        // Vendedor vê apenas sua carteira
        if (usuarioAtual != null && usuarioAtual.getPerfil() == PerfilUsuario.VENDEDOR) {
            Long vendedorId = usuarioAtual.getVendedor() != null ? usuarioAtual.getVendedor().getId() : -1L;
            spec = spec.and((root, q, cb) -> cb.equal(root.get("vendedor").get("id"), vendedorId));
        }

        if (filtro != null) {
            if (StringUtils.hasText(filtro.busca())) {
                String like = "%" + filtro.busca().toLowerCase() + "%";
                spec = spec.and((root, q, cb) -> cb.or(
                        cb.like(cb.lower(root.get("nome")), like),
                        cb.like(cb.lower(root.get("municipio")), like),
                        cb.like(cb.lower(root.get("cultura")), like),
                        cb.like(cb.lower(root.get("cpf")), like)
                ));
            }
            if (filtro.status() != null)
                spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), filtro.status()));
            if (filtro.vendedorId() != null)
                spec = spec.and((root, q, cb) -> cb.equal(root.get("vendedor").get("id"), filtro.vendedorId()));
            if (filtro.qualificado() != null)
                spec = spec.and((root, q, cb) -> cb.equal(root.get("qualificado"), filtro.qualificado()));
        }
        return clienteRepository.findAll(spec);
    }

    /** Ao abrir a ficha, registra tentativa no log de produtividade (obs. do fluxo, item 4). */
    public Cliente obter(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", id));

        Usuario usuarioAtual = obterUsuarioAtual();
        if (usuarioAtual != null && usuarioAtual.getPerfil() == PerfilUsuario.VENDEDOR
                && usuarioAtual.getVendedor() != null) {
            // Verifica se o cliente pertence ao vendedor
            if (cliente.getVendedor() == null
                    || !cliente.getVendedor().getId().equals(usuarioAtual.getVendedor().getId())) {
                throw new BusinessException("Acesso negado: este cliente não está em sua carteira.");
            }
            logProdutividadeService.registrar(
                    usuarioAtual.getVendedor().getId(), id, "ABERTURA",
                    "Ficha aberta pelo vendedor");
        }
        return cliente;
    }

    @Transactional
    public Cliente criar(ClienteRequest req) {
        return clienteRepository.save(Cliente.builder()
                .nome(req.nome()).cpf(req.cpf()).propriedade(req.propriedade())
                .cultura(req.cultura()).municipio(req.municipio())
                .telefone(req.telefone()).email(req.email())
                .status(StatusCliente.PENDENTE).qualificado(false)
                .build());
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest req) {
        Cliente c = buscarOuFalhar(id);
        c.setNome(req.nome()); c.setCpf(req.cpf()); c.setPropriedade(req.propriedade());
        c.setCultura(req.cultura()); c.setMunicipio(req.municipio());
        c.setTelefone(req.telefone()); c.setEmail(req.email());
        return clienteRepository.save(c);
    }

    @Transactional
    public void remover(Long id) { clienteRepository.delete(buscarOuFalhar(id)); }

    @Transactional
    public Cliente atualizarStatus(Long id, AtualizarStatusRequest req) {
        Cliente c = buscarOuFalhar(id);
        c.setStatus(req.status());
        return clienteRepository.save(c);
    }

    /**
     * Atualização de atendimento pelo vendedor — cobre os itens 4.1, 4.2 e 4.3 do fluxo.
     * Registra no log de produtividade e audita no log do sistema (requer senha confirmada
     * pelo front-end via POST /api/usuarios/verificar-senha antes de chamar este endpoint).
     */
    @Transactional
    public Cliente atualizarAtendimento(Long id, AtualizarAtendimentoRequest req, String ip) {
        Cliente c = buscarOuFalhar(id);
        Usuario usuarioAtual = obterUsuarioAtual();

        c.setStatus(req.status());
        c.setObservacao(req.observacao());

        if (req.status() == StatusCliente.VENDIDO) {
            c.setConvenio(req.convenio());
            c.setPrazo(req.prazo());
            c.setValorVendido(req.valorVendido());
        }
        clienteRepository.save(c);

        // Log de produtividade
        if (usuarioAtual != null && usuarioAtual.getVendedor() != null) {
            logProdutividadeService.registrar(
                    usuarioAtual.getVendedor().getId(), id,
                    req.status().name(), req.observacao());
        }

        // Auditoria no log do sistema
        if (usuarioAtual != null) {
            logSistemaService.registrar(usuarioAtual.getUsername(),
                    "ATUALIZAR_ATENDIMENTO", "cliente", id,
                    "Status: " + req.status() + " | " + req.observacao(), ip);
        }
        return c;
    }

    public Cliente buscarOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", id));
    }

    private Usuario obterUsuarioAtual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return usuarioRepository.findByUsername(auth.getName()).orElse(null);
    }
}
