package br.com.bbts.crm.cliente.controller;

import br.com.bbts.crm.cliente.dto.*;
import br.com.bbts.crm.cliente.service.ClienteService;
import br.com.bbts.crm.common.enums.StatusCliente;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusCliente status,
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(required = false) Boolean qualificado
    ) {
        return clienteService.listar(new ClienteFiltroDTO(busca, status, vendedorId, qualificado))
                .stream().map(ClienteDTO::from).toList();
    }

    @GetMapping("/{id}")
    public ClienteDTO obter(@PathVariable Long id) {
        return ClienteDTO.from(clienteService.obter(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO criar(@Valid @RequestBody ClienteRequest req) {
        return ClienteDTO.from(clienteService.criar(req));
    }

    @PutMapping("/{id}")
    public ClienteDTO atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return ClienteDTO.from(clienteService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) { clienteService.remover(id); }

    @PatchMapping("/{id}/status")
    public ClienteDTO atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRequest req) {
        return ClienteDTO.from(clienteService.atualizarStatus(id, req));
    }

    /**
     * Endpoint exclusivo do vendedor — atualiza o resultado do atendimento (4.1 / 4.2 / 4.3).
     * O front deve confirmar a senha via /api/usuarios/verificar-senha antes de chamar este endpoint.
     */
    @PatchMapping("/{id}/atendimento")
    public ClienteDTO atualizarAtendimento(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarAtendimentoRequest req,
            HttpServletRequest httpReq
    ) {
        return ClienteDTO.from(clienteService.atualizarAtendimento(id, req, httpReq.getRemoteAddr()));
    }
}
