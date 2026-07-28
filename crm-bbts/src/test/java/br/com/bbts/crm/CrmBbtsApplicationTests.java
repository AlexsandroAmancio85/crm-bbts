package br.com.bbts.crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class CrmBbtsApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que o contexto Spring sobe corretamente com o profile "dev" (H2).
    }
}
