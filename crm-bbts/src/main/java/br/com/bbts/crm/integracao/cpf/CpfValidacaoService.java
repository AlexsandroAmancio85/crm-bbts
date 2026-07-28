package br.com.bbts.crm.integracao.cpf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Valida CPFs via API da Receita Federal (item 8.1 do fluxo).
 *
 * Usa RestClient (Spring 6.1 / Boot 3.2+) — sem dependência de WebFlux.
 *
 * Em produção: configurar as variáveis de ambiente
 *   CRF_API_URL=https://api.cpf.receita.gov.br
 *   CRF_API_TOKEN=<token-homologado>
 *
 * Enquanto não configuradas, a validação roda apenas no modo local
 * (algoritmo dos dígitos verificadores), sem chamada HTTP externa.
 */
@Service
@Slf4j
public class CpfValidacaoService {

    private final RestClient restClient;
    private final boolean integracaoAtiva;

    public CpfValidacaoService(
            @Value("${crm.integracao.cpf.url:}") String apiUrl,
            @Value("${crm.integracao.cpf.token:}") String apiToken
    ) {
        this.integracaoAtiva = !apiUrl.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl.isBlank() ? "https://placeholder.local" : apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public CpfValidacaoResult validar(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (!validarFormatoLocal(cpfLimpo)) {
            return new CpfValidacaoResult(cpfLimpo, false, "INVALIDO",
                    "CPF inválido — dígitos verificadores incorretos", null);
        }

        if (!integracaoAtiva) {
            log.info("Integração Receita Federal não configurada. Validação local OK para CPF: {}", cpfLimpo);
            return new CpfValidacaoResult(cpfLimpo, true, "REGULAR",
                    "Validação local (offline) — configure CRF_API_URL para consulta real", null);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get()
                    .uri("/v1/cpf/{cpf}", cpfLimpo)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return new CpfValidacaoResult(cpfLimpo, false, "ERRO", "Sem resposta da Receita Federal", null);
            }

            String situacao = (String) response.get("situacao");
            String nome     = (String) response.get("nome");
            boolean regular = "REGULAR".equalsIgnoreCase(situacao);
            return new CpfValidacaoResult(cpfLimpo, regular, situacao, null, nome);

        } catch (RestClientException e) {
            log.error("Erro ao consultar CPF na Receita Federal: {}", e.getMessage());
            return new CpfValidacaoResult(cpfLimpo, false, "ERRO",
                    "Falha na conexão com a Receita Federal: " + e.getMessage(), null);
        }
    }

    /** Valida os dígitos verificadores do CPF (algoritmo oficial — funciona offline). */
    public boolean validarFormatoLocal(String cpf) {
        String c = cpf.replaceAll("[^0-9]", "");
        if (c.length() != 11 || c.chars().distinct().count() == 1) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (c.charAt(i) - '0') * (10 - i);
        int d1 = (sum * 10) % 11;
        if (d1 == 10) d1 = 0;
        if (d1 != c.charAt(9) - '0') return false;

        sum = 0;
        for (int i = 0; i < 10; i++) sum += (c.charAt(i) - '0') * (11 - i);
        int d2 = (sum * 10) % 11;
        if (d2 == 10) d2 = 0;
        return d2 == c.charAt(10) - '0';
    }

    public record CpfValidacaoResult(
            String cpf,
            boolean valido,
            String situacao,
            String mensagemErro,
            String nomeReceita
    ) {}
}
