package br.com.neves.api_boleto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Boletos")
                .version("1.0")
                .description("API para gerenciamento de boletos bancários")
                .contact(new Contact()
                        .name("Victor Neves")
                        .email("victorfneves99@gmail.com")));
    }
}
