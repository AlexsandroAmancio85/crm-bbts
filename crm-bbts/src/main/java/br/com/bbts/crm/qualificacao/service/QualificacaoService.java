package br.com.bbts.crm.qualificacao.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.qualificacao.dto.QualificarRequest;
import br.com.bbts.crm.qualificacao.dto.RejeitarRequest;
import br.com.bbts.crm.qualificacao.entity.QualificacaoCliente;
import br.com.bbts.crm.qualificacao.repository.QualificacaoClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Etapa 4 do fluxo: Qualificação Gerencial. */
@Service
@RequiredArgsConstructor
public class QualificacaoService {

    private final ClienteRepository clienteRepository;
    private final QualificacaoClienteRepository qualificacaoClienteRepository;

    public List<Cliente> listarPendentes() {
        return clienteRepository.findByQualificadoFalse();
    }

    @Transactional
    public Cliente qualificar(Long clienteId, QualificarRequest request) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.setQualificado(true);
        clienteRepository.save(cliente);

        qualificacaoClienteRepository.save(QualificacaoCliente.builder()
                .cliente(cliente)
                .aprovado(true)
                .qualificadoPor(request != null ? request.qualificadoPor() : null)
                .build());

        return cliente;
    }

    @Transactional
    public Cliente rejeitar(Long clienteId, RejeitarRequest request) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.setQualificado(false);
        cliente.setStatus(StatusCliente.INDISPONIVEL);
        clienteRepository.save(cliente);

        qualificacaoClienteRepository.save(QualificacaoCliente.builder()
                .cliente(cliente)
                .aprovado(false)
                .motivoRejeicao(request.motivo())
                .build());

        return cliente;
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.of("Cliente", id));
    }
}
