package br.com.bbts.crm.contato.controller;

import br.com.bbts.crm.contato.dto.ContatoDTO;
import br.com.bbts.crm.contato.dto.ContatoRequest;
import br.com.bbts.crm.contato.service.ContatoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@Tag(name = "Contatos")
public class ContatoController {

    private final ContatoService contatoService;

    @GetMapping("/cliente/{clienteId}")
    public List<ContatoDTO> listarPorCliente(@PathVariable Long clienteId) {
        return contatoService.listarPorCliente(clienteId).stream().map(ContatoDTO::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContatoDTO registrar(@Valid @RequestBody ContatoRequest request) {
        return ContatoDTO.from(contatoService.registrar(request));
    }
}
