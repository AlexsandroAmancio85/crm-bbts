package br.com.bbts.crm.vendedor.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.exception.BusinessException;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.vendedor.dto.DistribuicaoRequest;
import br.com.bbts.crm.vendedor.dto.VendedorDTO;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final VendedorRepository vendedorRepository;
    private final ClienteRepository clienteRepository;

    public List<VendedorDTO> listar() {
        return vendedorRepository.findAll().stream()
                .map(v -> VendedorDTO.from(v, clienteRepository.countByVendedorId(v.getId())))
                .toList();
    }

    public List<Cliente> carteira(Long vendedorId) {
        buscarOuFalhar(vendedorId);
        return clienteRepository.findByVendedorId(vendedorId);
    }

    @Transactional
    public Cliente distribuir(DistribuicaoRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", request.clienteId()));
        Vendedor vendedor = buscarOuFalhar(request.vendedorId());

        if (!cliente.isQualificado()) {
            throw new BusinessException("Cliente ainda não foi qualificado pela gerência (etapa 4 do fluxo).");
        }

        cliente.setVendedor(vendedor);
        if (cliente.getStatus() == StatusCliente.PENDENTE) {
            cliente.setStatus(StatusCliente.PENDENTE); // permanece pendente até o 1º contato
        }
        return clienteRepository.save(cliente);
    }

    private Vendedor buscarOuFalhar(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", id));
    }
}
