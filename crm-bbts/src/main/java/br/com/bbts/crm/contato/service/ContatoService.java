package br.com.bbts.crm.contato.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.contato.dto.ContatoRequest;
import br.com.bbts.crm.contato.entity.ContatoCliente;
import br.com.bbts.crm.contato.repository.ContatoClienteRepository;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoClienteRepository contatoClienteRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public List<ContatoCliente> listarPorCliente(Long clienteId) {
        return contatoClienteRepository.findByClienteIdOrderByDataContatoDesc(clienteId);
    }

    @Transactional
    public ContatoCliente registrar(ContatoRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));

        Vendedor vendedor = null;
        if (request.vendedorId() != null) {
            vendedor = vendedorRepository.findById(request.vendedorId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", request.vendedorId()));
        }

        ContatoCliente contato = ContatoCliente.builder()
                .cliente(cliente)
                .vendedor(vendedor)
                .canal(request.canal())
                .observacao(request.observacao())
                .build();
        return contatoClienteRepository.save(contato);
    }
}
