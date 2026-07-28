package br.com.bbts.crm.oportunidade.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.oportunidade.dto.OportunidadeRequest;
import br.com.bbts.crm.oportunidade.entity.Oportunidade;
import br.com.bbts.crm.oportunidade.repository.OportunidadeRepository;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OportunidadeService {

    private final OportunidadeRepository oportunidadeRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public List<Oportunidade> listarPorCliente(Long clienteId) {
        return oportunidadeRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Oportunidade criar(OportunidadeRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));
        Vendedor vendedor = request.vendedorId() != null
                ? vendedorRepository.findById(request.vendedorId())
                        .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", request.vendedorId()))
                : null;

        Oportunidade oportunidade = Oportunidade.builder()
                .cliente(cliente)
                .vendedor(vendedor)
                .descricao(request.descricao())
                .valorEstimado(request.valorEstimado())
                .build();
        return oportunidadeRepository.save(oportunidade);
    }
}
