package br.com.bbts.crm.agenda.service;

import br.com.bbts.crm.agenda.dto.AgendarRequest;
import br.com.bbts.crm.agenda.entity.RetornoAgendado;
import br.com.bbts.crm.agenda.repository.RetornoAgendadoRepository;
import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final RetornoAgendadoRepository retornoAgendadoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public List<RetornoAgendado> listar(Long vendedorId) {
        return vendedorId != null
                ? retornoAgendadoRepository.findByVendedorIdOrderByDataAgendadaAsc(vendedorId)
                : retornoAgendadoRepository.findAllByOrderByDataAgendadaAsc();
    }

    @Transactional
    public RetornoAgendado agendar(AgendarRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));
        Vendedor vendedor = vendedorRepository.findById(request.vendedorId())
                .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", request.vendedorId()));

        RetornoAgendado retorno = RetornoAgendado.builder()
                .cliente(cliente)
                .vendedor(vendedor)
                .dataAgendada(request.dataAgendada())
                .observacao(request.observacao())
                .concluido(false)
                .build();
        return retornoAgendadoRepository.save(retorno);
    }

    @Transactional
    public RetornoAgendado concluir(Long id) {
        RetornoAgendado retorno = retornoAgendadoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Agendamento", id));
        retorno.setConcluido(true);
        return retornoAgendadoRepository.save(retorno);
    }
}
