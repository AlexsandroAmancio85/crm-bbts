package br.com.bbts.crm.qualificacao;

import org.springframework.beans.factory.annotation.Value;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class QualificacaoIaService {

    
    @Value("${gemini.api-key}")
    private String API_KEY;

    @Value("${gemini.api-url}")
    private String GEMINI_URL;

    public QualificacaoResponse analisarLead(LeadDto lead) {
        try {
            String prompt = """
                Determine o nível de qualificação e prioridade do lead abaixo.
                Você DEVE responder estritamente com um objeto JSON válido, sem usar as marcações markdown de bloco de código (```json ou ```).
                
                Campos obrigatórios no JSON de retorno:
                {
                  "prioridade": "ALTA, MEDIA ou BAIXA",
                  "justificativa": "Escreva a justificativa comercial",
                  "proximoPassoRecomendado": "Escreva a recomendação de ação"
                }

                Dados do Lead:
                - Nome: %s
                - Empresa: %s
                - Faturamento Estimado: R$ %.2f
                - Funcionários: %d
                - Segmento: %s
                - Histórico: %s
                """.formatted(
                    lead.nome(), lead.empresa(), lead.faturamentoEstimado(), 
                    lead.quantidadeFuncionarios(), lead.segmento(), lead.historicoContato()
                );

            // Monta o corpo da requisição exatamente no formato que a API do Google exige
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // A partir da migração do Google para chaves "AQ." (Auth keys), a autenticação
            // deve ser feita via header x-goog-api-key, não mais via query param "?key=".
            headers.set("x-goog-api-key", API_KEY);

            // Estrutura do JSON do Gemini: { "contents": [{ "parts": [{ "text": "prompt" }] }] }
            JsonObject body = new JsonObject();
            JsonArray contents = new JsonArray();
            JsonObject contentObj = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject partObj = new JsonObject();
            
            partObj.addProperty("text", prompt);
            parts.add(partObj);
            contentObj.add("parts", parts);
            contents.add(contentObj);
            body.add("contents", contents);

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
            
            // Faz a chamada HTTP POST para o Google (URL sem a chave, ela vai no header)
            String responseStr = restTemplate.postForObject(GEMINI_URL, entity, String.class);

            // Extrai o texto de dentro da resposta do Google usando o Gson
            JsonObject jsonResponse = new Gson().fromJson(responseStr, JsonObject.class);
            String rawIaText = jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text")
                    .getAsString();

            // Converte o texto plano retornado no seu objeto QualificacaoResponse
            return new Gson().fromJson(rawIaText.trim(), QualificacaoResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro na comunicação direta com o Gemini: " + e.getMessage(), e);
        }
    }
}