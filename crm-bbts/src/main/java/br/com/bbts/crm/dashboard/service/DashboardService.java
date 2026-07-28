package br.com.bbts.crm.dashboard.service;

import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.dashboard.dto.KpisDTO;
import br.com.bbts.crm.dashboard.dto.TrilhaDTO;
import br.com.bbts.crm.importacao.repository.ArquivoImportadoRepository;
import br.com.bbts.crm.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/** Consolida a visão das 8 etapas do fluxo para o Dashboard / Visão geral. */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final ArquivoImportadoRepository arquivoImportadoRepository;
    private final VendaRepository vendaRepository;

    public TrilhaDTO resumoTrilha() {
        long totalClientes = clienteRepository.count();
        long qualificados = clienteRepository.countByQualificadoTrue();
        long emAtendimento = clienteRepository.count() - clienteRepository.countByStatus(StatusCliente.PENDENTE);

        Map<String, Long> contagens = new LinkedHashMap<>();
        contagens.put("recebimento", (long) arquivoImportadoRepository.findAll().stream()
                .mapToInt(a -> a.getLinhasLidas()).sum());
        contagens.put("importacao", totalClientes);
        contagens.put("validacao", totalClientes);
        contagens.put("qualificacao", qualificados);
        contagens.put("distribuicao", clienteRepository.findAll().stream().filter(c -> c.getVendedor() != null).count());
        contagens.put("atendimento", emAtendimento);
        contagens.put("monitoramento", emAtendimento);
        contagens.put("relatorios", vendaRepository.count());

        return new TrilhaDTO(contagens);
    }

    public KpisDTO kpis() {
        long total = clienteRepository.count();
        long qualificados = clienteRepository.countByQualificadoTrue();
        long vendidos = clienteRepository.countByStatus(StatusCliente.VENDIDO);
        double taxa = total == 0 ? 0 : (vendidos * 100.0) / total;
        return new KpisDTO(total, qualificados, vendidos, taxa);
    }
}
