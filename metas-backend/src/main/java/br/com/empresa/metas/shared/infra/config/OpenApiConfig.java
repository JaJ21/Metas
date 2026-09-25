package br.com.empresa.metas.shared.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura a documentação automática da API (Swagger UI em /docs).
 * Também registra o esquema "Bearer" (JWT), pra dar pra testar os
 * endpoints protegidos direto pela tela do Swagger, colando o token.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI metasOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Metas Backend API")
                        .description("API de upload e controle de metas/orçamento por Centro de Custo e Código de Conta.")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
