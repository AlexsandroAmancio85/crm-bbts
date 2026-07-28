package br.com.bbts.crm.logprodutividade.controller;

import br.com.bbts.crm.logprodutividade.dto.LogProdutividadeDTO;
import br.com.bbts.crm.logprodutividade.dto.ResumoProdutividadeDTO;
import br.com.bbts.crm.logprodutividade.service.LogProdutividadeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs/produtividade")
@RequiredArgsConstructor
@Tag(name = "Log de Produtividade")
public class LogProdutividadeController {

    private final LogProdutividadeService service;

    @GetMapping("/vendedor/{vendedorId}")
    public List<LogProdutividadeDTO> porVendedor(@PathVariable Long vendedorId) {
        return service.listarPorVendedor(vendedorId).stream().map(LogProdutividadeDTO::from).toList();
    }

    @GetMapping("/vendedor/{vendedorId}/resumo")
    public List<ResumoProdutividadeDTO> resumo(@PathVariable Long vendedorId) {
        return service.resumoPorVendedor(vendedorId);
    }

    @GetMapping("/vendedor/{vendedorId}/periodo")
    public List<LogProdutividadeDTO> porPeriodo(
            @PathVariable Long vendedorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return service.listarPorPeriodo(vendedorId, inicio, fim).stream().map(LogProdutividadeDTO::from).toList();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<LogProdutividadeDTO> porCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId).stream().map(LogProdutividadeDTO::from).toList();
    }
}
