package br.com.bbts.crm.logprodutividade.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.logprodutividade.dto.ResumoProdutividadeDTO;
import br.com.bbts.crm.logprodutividade.entity.LogProdutividade;
import br.com.bbts.crm.logprodutividade.repository.LogProdutividadeRepository;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogProdutividadeService {

    private final LogProdutividadeRepository logRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    @Transactional
    public LogProdutividade registrar(Long vendedorId, Long clienteId, String tipo, String obs) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", vendedorId));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> ResourceNotFoundException.of("Cliente", clienteId));
        return logRepository.save(LogProdutividade.builder()
                .vendedor(vendedor).cliente(cliente)
                .tipoAtividade(tipo).observacao(obs).build());
    }

    public List<LogProdutividade> listarPorVendedor(Long vendedorId) {
        return logRepository.findByVendedorIdOrderByDataHoraDesc(vendedorId);
    }

    public List<LogProdutividade> listarPorCliente(Long clienteId) {
        return logRepository.findByClienteIdOrderByDataHoraDesc(clienteId);
    }

    public List<LogProdutividade> listarPorPeriodo(Long vendedorId, LocalDateTime inicio, LocalDateTime fim) {
        return logRepository.findByVendedorAndPeriodo(vendedorId, inicio, fim);
    }

    public List<ResumoProdutividadeDTO> resumoPorVendedor(Long vendedorId) {
        return logRepository.contarPorTipoAtividade(vendedorId).stream()
                .map(row -> new ResumoProdutividadeDTO((String) row[0], (Long) row[1]))
                .toList();
    }
}
