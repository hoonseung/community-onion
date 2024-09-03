package com.onion.backend.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Community Onion API")
                .description("API documentation for my Spring Boot application")
                .version("1.0")
                .contact(new Contact()
                    .name("hoonseung")
                    .email("xxx@gmail.com")
                    .url("https://localhost.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                .description("추가적인 문서")
                .url("https://example.com"));
    }

}
