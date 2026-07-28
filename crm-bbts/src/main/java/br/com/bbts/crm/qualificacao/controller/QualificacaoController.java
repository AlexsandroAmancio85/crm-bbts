package br.com.bbts.crm.qualificacao.controller;

import br.com.bbts.crm.qualificacao.LeadDto;
import br.com.bbts.crm.qualificacao.QualificacaoResponse;
import br.com.bbts.crm.qualificacao.QualificacaoIaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qualificacao")
@CrossOrigin(origins = "http://localhost:5173")
public class QualificacaoController {

    private final QualificacaoIaService qualificacaoService;

    // Injete o service aqui (se já tiver outro construtor, adicione o service nele)
    public QualificacaoController(QualificacaoIaService qualificacaoService) {
        this.qualificacaoService = qualificacaoService;
    }

    @PostMapping("/analisar")
    public ResponseEntity<QualificacaoResponse> qualificarLead(@RequestBody LeadDto leadDto) {
        QualificacaoResponse resultado = qualificacaoService.analisarLead(leadDto);
        return ResponseEntity.ok(resultado);
    }
}