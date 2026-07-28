package br.com.bbts.crm.integracao.dataprev;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integracao/dataprev")
@RequiredArgsConstructor
@Tag(name = "Integração - Dataprev (Margens)")
public class DataprevController {

    private final DataprevService service;

    @GetMapping("/margem/{cpf}")
    public DataprevService.ConsultaMargem consultarMargem(@PathVariable String cpf) {
        return service.consultarMargem(cpf);
    }
}
