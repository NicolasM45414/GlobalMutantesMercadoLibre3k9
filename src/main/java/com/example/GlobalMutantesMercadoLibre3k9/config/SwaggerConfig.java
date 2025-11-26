package com.example.GlobalMutantesMercadoLibre3k9.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
/**
 * Configuración de Swagger/OpenAPI para documentación de la API.
 *
 * Acceso: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String applicationPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server developmentServer = new Server();
        developmentServer.setUrl("http://localhost:" + applicationPort);
        developmentServer.setDescription("Local Development Server");

        Contact supportContact = new Contact();
        supportContact.setName("Mutant Detector Team");
        supportContact.setEmail("support@mutantdetector.com");

        License projectLicense = new License();
        projectLicense.setName("MIT License");
        projectLicense.setUrl("https://opensource.org/licenses/MIT");

        Info apiMetadata = new Info()
                .title("Mutant Detector API")
                .version("1.0.0")
                .description("API REST para detectar mutantes mediante análisis de secuencias de ADN. " +
                        "Magneto quiere reclutar mutantes para luchar contra los X-Men y necesita " +
                        "una forma automatizada de identificarlos.\n\n" +
                        "**Criterio de Detección:**\n" +
                        "Un humano es mutante si se encuentran **más de una secuencia** de 4 letras iguales " +
                        "(A, T, C, G) en dirección horizontal, vertical o diagonal.\n\n" +
                        "**Ejemplo de ADN Mutante:**\n" +
                        "```\n" +
                        "A T G C G A\n" +
                        "C A G T G C\n" +
                        "T T A T G T\n" +
                        "A G A A G G  ← Diagonal: A-A-A-A\n" +
                        "C C C C T A  ← Horizontal: C-C-C-C\n" +
                        "T C A C T G\n" +
                        "```")
                .contact(supportContact)
                .license(projectLicense);

        return new OpenAPI()
                .info(apiMetadata)
                .servers(List.of(developmentServer));
    }
}