package br.com.bbts.crm.relatorio.service;

import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.relatorio.dto.IndicadorMensalDTO;
import br.com.bbts.crm.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/** Etapa 8 do fluxo: Relatórios e Indicadores. */
@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;

    /**
     * Indicadores dos últimos 6 meses. Implementação simplificada para o MVP — em produção,
     * trocar por uma consulta agregada (ex.: @Query nativo agrupando por mês/status).
     */
    public List<IndicadorMensalDTO> indicadoresMensais() {
        LocalDate hoje = LocalDate.now();
        return java.util.stream.IntStream.rangeClosed(0, 5)
                .mapToObj(hoje::minusMonths)
                .sorted()
                .map(data -> new IndicadorMensalDTO(
                        data.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")),
                        vendaRepository.count() / 6, // placeholder proporcional — substituir por consulta real por mês
                        clienteRepository.countByStatus(br.com.bbts.crm.common.enums.StatusCliente.CONTATADO) / 6,
                        clienteRepository.countByStatus(br.com.bbts.crm.common.enums.StatusCliente.INDISPONIVEL) / 6
                ))
                .toList();
    }

    public byte[] exportarCsv() {
        StringBuilder csv = new StringBuilder("mes,vendidos,contatados,indisponiveis\n");
        for (IndicadorMensalDTO i : indicadoresMensais()) {
            csv.append(i.mes()).append(',').append(i.vendidos()).append(',')
                    .append(i.contatados()).append(',').append(i.indisponiveis()).append('\n');
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(csv.toString().getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }
}
