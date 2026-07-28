package br.com.bbts.crm.oportunidade.controller;

import br.com.bbts.crm.oportunidade.dto.OportunidadeDTO;
import br.com.bbts.crm.oportunidade.dto.OportunidadeRequest;
import br.com.bbts.crm.oportunidade.service.OportunidadeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
@Tag(name = "Oportunidades")
public class OportunidadeController {

    private final OportunidadeService oportunidadeService;

    @GetMapping("/cliente/{clienteId}")
    public List<OportunidadeDTO> listarPorCliente(@PathVariable Long clienteId) {
        return oportunidadeService.listarPorCliente(clienteId).stream().map(OportunidadeDTO::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OportunidadeDTO criar(@Valid @RequestBody OportunidadeRequest request) {
        return OportunidadeDTO.from(oportunidadeService.criar(request));
    }
}
