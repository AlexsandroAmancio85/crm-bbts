package br.com.bbts.crm.integracao.cpf;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integracao/cpf")
@RequiredArgsConstructor
@Tag(name = "Integração - CPF Receita Federal")
public class CpfValidacaoController {

    private final CpfValidacaoService service;

    @GetMapping("/{cpf}")
    public CpfValidacaoService.CpfValidacaoResult validar(@PathVariable String cpf) {
        return service.validar(cpf);
    }
}
