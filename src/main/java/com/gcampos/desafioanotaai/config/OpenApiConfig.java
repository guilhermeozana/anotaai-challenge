package com.gcampos.desafioanotaai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Catalog API")
                        .version("v1")
                        .description("Microservice built for Anota AI challenge")
                        .license( new License()
                                .name("Anota AI Challenge")
                                .url("https://github.com/githubanotaai/new-test-backend-nodejs")));
    }
}
