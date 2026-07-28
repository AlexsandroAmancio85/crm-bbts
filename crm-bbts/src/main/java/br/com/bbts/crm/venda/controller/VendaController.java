package br.com.bbts.crm.venda.controller;

import br.com.bbts.crm.venda.dto.VendaDTO;
import br.com.bbts.crm.venda.dto.VendaRequest;
import br.com.bbts.crm.venda.service.VendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@Tag(name = "Vendas")
public class VendaController {

    private final VendaService vendaService;

    @GetMapping
    public List<VendaDTO> listar() {
        return vendaService.listar().stream().map(VendaDTO::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaDTO registrar(@Valid @RequestBody VendaRequest request) {
        return VendaDTO.from(vendaService.registrar(request));
    }
}
