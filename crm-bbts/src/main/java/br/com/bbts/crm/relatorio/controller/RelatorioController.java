package br.com.bbts.crm.relatorio.controller;

import br.com.bbts.crm.relatorio.dto.IndicadorMensalDTO;
import br.com.bbts.crm.relatorio.service.RelatorioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Etapa 8 do fluxo: Relatórios e Indicadores. */
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/indicadores-mensais")
    public List<IndicadorMensalDTO> indicadoresMensais() {
        return relatorioService.indicadoresMensais();
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() {
        byte[] csv = relatorioService.exportarCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-crm-bbts.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
