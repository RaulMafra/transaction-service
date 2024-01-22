package com.safeway.test.doc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerOpenApi(){
        return new OpenAPI()
                .info(new Info().title("RestAPI Transaction")
                        .description("Service sample transaction")
                        .version("v0.0.1"));
    }
}
