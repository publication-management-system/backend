package com.pms.publicationmanagement.config.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String BEARER = "bearer";
    public static final String SECURITY_SCHEME_KEY = "SwaggerAuthentication";
    public static final String JWT = "JWT";

    @Bean
    public OpenAPI configureSwaggerAuth() {
        SecurityScheme jwtScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .scheme(BEARER).bearerFormat(JWT);
        return new OpenAPI().components(new Components().addSecuritySchemes(SECURITY_SCHEME_KEY, jwtScheme));
    }
}
