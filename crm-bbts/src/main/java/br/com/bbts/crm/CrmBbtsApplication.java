package br.com.bbts.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrmBbtsApplication {

    public static void main(String[] args) {
        // Define explicitamente o tipo SERVLET (Tomcat) — garante que nenhuma lib
        // transitiva de contexto reativo (Netty/WebFlux) altere o comportamento.
        SpringApplication app = new SpringApplication(CrmBbtsApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}
