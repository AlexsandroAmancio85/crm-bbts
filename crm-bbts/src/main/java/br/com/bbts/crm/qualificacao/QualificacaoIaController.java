package br.com.bbts.crm.qualificacao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/qualificacao-ia")
@CrossOrigin(origins = "http://localhost:5173")
public class QualificacaoIaController {

    private final QualificacaoIaService qualificacaoIaService;

    public QualificacaoIaController(QualificacaoIaService qualificacaoIaService) {
        this.qualificacaoIaService = qualificacaoIaService;
    }

    @PostMapping("/analisar")
    public ResponseEntity<?> qualificarLead(@RequestBody LeadDto leadDto) {
        try {
            QualificacaoResponse resultado = qualificacaoIaService.analisarLead(leadDto);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            // Força o envio da mensagem real do erro para o Postman
            return ResponseEntity.badRequest().body(Map.of(
                "erro", true,
                "mensagem_real", e.getMessage()
            ));
        }
    }
}