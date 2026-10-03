package com.civa.app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME_FORMAT = "JWT";
    private static final String SCHEME_DESCRIPTION  = "JWT Authentication para la Api";

    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()

                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(
                    new Components()
                        .addSecuritySchemes(SCHEME_NAME, 
                            new SecurityScheme()
                                .name(SCHEME_NAME) 
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat(SCHEME_FORMAT)
                                .description(SCHEME_DESCRIPTION)
                    )
                )
                .info(new Info()
                        .title("Civa API")
                        .version("1.0")
                        .description("API documentation for Civa application")
                        .contact(new Contact()
                            .name("Civa Support")
                            .email("civa-support@civa.com")
                            .url("https://www.civa.com"))
                        .license(new License()
                            .name("Apache 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                    );
    }

}
