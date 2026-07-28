package br.com.bbts.crm.vendedor.controller;

import br.com.bbts.crm.cliente.dto.ClienteDTO;
import br.com.bbts.crm.vendedor.dto.DistribuicaoRequest;
import br.com.bbts.crm.vendedor.dto.VendedorDTO;
import br.com.bbts.crm.vendedor.service.VendedorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Etapa 5 do fluxo: Distribuição aos Vendedores. */
@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@Tag(name = "Vendedores")
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    public List<VendedorDTO> listar() {
        return vendedorService.listar();
    }

    @GetMapping("/{id}/carteira")
    public List<ClienteDTO> carteira(@PathVariable Long id) {
        return vendedorService.carteira(id).stream().map(ClienteDTO::from).toList();
    }

    @PostMapping("/distribuicao")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDTO distribuir(@Valid @RequestBody DistribuicaoRequest request) {
        return ClienteDTO.from(vendedorService.distribuir(request));
    }
}
