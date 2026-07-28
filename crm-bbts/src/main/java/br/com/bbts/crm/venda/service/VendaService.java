package br.com.bbts.crm.venda.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import br.com.bbts.crm.venda.dto.VendaRequest;
import br.com.bbts.crm.venda.entity.Venda;
import br.com.bbts.crm.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Registro do desfecho "Vendido" da etapa 6 — Atendimento do Vendedor. */
@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public List<Venda> listar() {
        return vendaRepository.findAll();
    }

    @Transactional
    public Venda registrar(VendaRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));
        Vendedor vendedor = vendedorRepository.findById(request.vendedorId())
                .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", request.vendedorId()));

        Venda venda = Venda.builder()
                .cliente(cliente)
                .vendedor(vendedor)
                .valor(request.valor())
                .observacao(request.observacao())
                .build();
        venda = vendaRepository.save(venda);

        cliente.setStatus(StatusCliente.VENDIDO);
        clienteRepository.save(cliente);

        return venda;
    }
}
