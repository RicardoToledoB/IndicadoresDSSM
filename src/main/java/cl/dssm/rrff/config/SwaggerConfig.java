package cl.dssm.rrff.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Documentacion de endpoints reales. El contrato R05 en /docs es una referencia aparte. */
@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI rrffOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("RRFF-SSM — API del Servicio de Salud Magallanes")
                .version("0.1.0-SNAPSHOT")
                .description("Documentacion generada desde los controladores Spring Boot en ejecucion. "
                  + "Modulo prioritario 7.2 Carta Gantt. Los recursos 7.2 utilizan un controlador generico "
                  + "con {resource}; consultar el contrato R05 por separado como referencia propuesta. "
                  + "La escritura institucional permanece sujeta a autorizacion."))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Token devuelto por POST /api/v1/auth/login")));
    }
}
