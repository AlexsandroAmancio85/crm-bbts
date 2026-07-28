package br.com.bbts.crm.integracao.dataprev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Consulta margens de crédito consignado via APIs da Dataprev (item 8 do fluxo).
 *
 * Usa RestClient (Spring 6.1 / Boot 3.2+) — sem dependência de WebFlux.
 *
 * Em produção: configurar as variáveis de ambiente
 *   DATAPREV_API_URL=https://api.dataprev.gov.br
 *   DATAPREV_API_TOKEN=<token-do-convenio>
 *
 * Enquanto as credenciais não estiverem disponíveis, o serviço retorna
 * dados simulados (mock=true) para não bloquear o desenvolvimento/testes.
 */
@Service
@Slf4j
public class DataprevService {

    private final RestClient restClient;
    private final boolean integracaoAtiva;

    public DataprevService(
            @Value("${crm.integracao.dataprev.url:}") String apiUrl,
            @Value("${crm.integracao.dataprev.token:}") String apiToken
    ) {
        this.integracaoAtiva = !apiUrl.isBlank();
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl.isBlank() ? "https://placeholder.local" : apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ConsultaMargem consultarMargem(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (!integracaoAtiva) {
            log.info("Dataprev não configurada — retornando dados simulados para CPF {}", cpfLimpo);
            return ConsultaMargem.mock(cpfLimpo);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get()
                    .uri("/consignado/v1/margens/{cpf}", cpfLimpo)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return ConsultaMargem.erro(cpfLimpo, "Sem resposta da Dataprev");
            }

            return new ConsultaMargem(
                    cpfLimpo,
                    parseBD(response.get("beneficio")),
                    parseBD(response.get("margemDisponivel")),
                    parseBD(response.get("margemUtilizada")),
                    (String) response.get("especie"),
                    (String) response.get("situacao"),
                    false,
                    null
            );

        } catch (RestClientException e) {
            log.error("Erro ao consultar Dataprev: {}", e.getMessage());
            return ConsultaMargem.erro(cpfLimpo, "Falha na conexão com a Dataprev: " + e.getMessage());
        }
    }

    private BigDecimal parseBD(Object val) {
        if (val == null) return null;
        if (val instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        try { return new BigDecimal(val.toString()); } catch (Exception e) { return null; }
    }

    public record ConsultaMargem(
            String cpf,
            BigDecimal valorBeneficio,
            BigDecimal margemDisponivel,
            BigDecimal margemUtilizada,
            String especieBeneficio,
            String situacao,
            boolean mock,
            String mensagemErro
    ) {
        static ConsultaMargem mock(String cpf) {
            return new ConsultaMargem(cpf,
                    new BigDecimal("2450.00"),
                    new BigDecimal("735.00"),
                    new BigDecimal("210.00"),
                    "APOSENTADORIA POR IDADE", "ATIVO", true, null);
        }
        static ConsultaMargem erro(String cpf, String msg) {
            return new ConsultaMargem(cpf, null, null, null, null, "ERRO", false, msg);
        }
    }
}
