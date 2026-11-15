package cl.corschecars.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI corscheCarsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CorscheCars API")
                        .version("v1")
                        .description("API REST para consultar y administrar el catálogo de vehículos."));
    }
}
