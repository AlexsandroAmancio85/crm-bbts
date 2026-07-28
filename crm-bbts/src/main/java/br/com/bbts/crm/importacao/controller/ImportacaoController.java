package br.com.bbts.crm.importacao.controller;

import br.com.bbts.crm.importacao.dto.ImportacaoDTO;
import br.com.bbts.crm.importacao.dto.LogImportacaoDTO;
import br.com.bbts.crm.importacao.service.ImportacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/** Etapas 1-3 do fluxo: Recebimento, Importação e Leitura, Validação da Base. */
@RestController
@RequestMapping("/api/importacoes")
@RequiredArgsConstructor
@Tag(name = "Importação")
public class ImportacaoController {

    private final ImportacaoService importacaoService;

    @GetMapping
    public List<ImportacaoDTO> listar() {
        return importacaoService.listar().stream().map(ImportacaoDTO::from).toList();
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ImportacaoDTO upload(@RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        return ImportacaoDTO.from(importacaoService.importar(arquivo));
    }

    @PostMapping("/{id}/validar")
    public ImportacaoDTO validar(@PathVariable Long id) {
        return ImportacaoDTO.from(importacaoService.validar(id));
    }

    @GetMapping("/{id}/log")
    public List<LogImportacaoDTO> log(@PathVariable Long id) {
        return importacaoService.obterLog(id).stream().map(LogImportacaoDTO::from).toList();
    }
}
