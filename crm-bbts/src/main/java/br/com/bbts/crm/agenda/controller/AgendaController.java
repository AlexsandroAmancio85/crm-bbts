package br.com.bbts.crm.agenda.controller;

import br.com.bbts.crm.agenda.dto.AgendaDTO;
import br.com.bbts.crm.agenda.dto.AgendarRequest;
import br.com.bbts.crm.agenda.service.AgendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
@Tag(name = "Agenda")
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public List<AgendaDTO> listar(@RequestParam(required = false) Long vendedorId) {
        return agendaService.listar(vendedorId).stream().map(AgendaDTO::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendaDTO agendar(@Valid @RequestBody AgendarRequest request) {
        return AgendaDTO.from(agendaService.agendar(request));
    }

    @PatchMapping("/{id}/concluir")
    public AgendaDTO concluir(@PathVariable Long id) {
        return AgendaDTO.from(agendaService.concluir(id));
    }
}
