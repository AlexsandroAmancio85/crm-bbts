package br.com.bbts.crm.logsistema.controller;

import br.com.bbts.crm.logsistema.dto.LogSistemaDTO;
import br.com.bbts.crm.logsistema.service.LogSistemaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs/sistema")
@RequiredArgsConstructor
@Tag(name = "Log do Sistema")
public class LogSistemaController {

    private final LogSistemaService service;

    @GetMapping
    public List<LogSistemaDTO> recentes() {
        return service.listarRecentes().stream().map(LogSistemaDTO::from).toList();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<LogSistemaDTO> porUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId).stream().map(LogSistemaDTO::from).toList();
    }
}
