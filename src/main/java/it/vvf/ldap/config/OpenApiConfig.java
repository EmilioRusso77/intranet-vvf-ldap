package it.vvf.ldap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API di Gestione Ruoli Utente")
                        .version("1.0")
                        .description("API per la gestione dei ruoli utente e delle sezioni nell'intranet VVF"));
    }
}