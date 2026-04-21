package com.orbitshop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        // Schema for RFC 7807 ProblemDetail
        Schema<?> problemDetailSchema = new Schema<>()
                .type("object")
                .addProperty("type", new StringSchema().example("https://api.orbitshop.com/errors/not-found"))
                .addProperty("title", new StringSchema().example("Not Found"))
                .addProperty("status", new IntegerSchema().example(404))
                .addProperty("detail", new StringSchema().example("Product not found"))
                .addProperty("instance", new StringSchema().example("/api/v1/products/1"))
                .addProperty("timestamp", new DateTimeSchema().example("2026-01-01T12:00:00Z"))
                .addProperty("path", new StringSchema().example("/api/v1/products/1"))
                .addProperty("correlationId", new StringSchema().example("123e4567-e89b-12d3-a456-426614174000"));

        // Common API responses
        ApiResponse badRequestResponse = createApiResponse("Bad Request", problemDetailSchema);
        ApiResponse unauthorizedResponse = createApiResponse("Unauthorized", problemDetailSchema);
        ApiResponse forbiddenResponse = createApiResponse("Forbidden", problemDetailSchema);
        ApiResponse notFoundResponse = createApiResponse("Not Found", problemDetailSchema);
        ApiResponse internalServerErrorResponse = createApiResponse("Internal Server Error", problemDetailSchema);

        return new OpenAPI()
                .info(new Info()
                        .title("OrbitShop E-commerce API")
                        .description("REST API for the OrbitShop e-commerce platform.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("OrbitShop Team")
                                .email("support@orbitshop.com")
                                .url("https://api.orbitshop.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSchemas("ProblemDetail", problemDetailSchema)
                        .addResponses("BadRequest", badRequestResponse)
                        .addResponses("Unauthorized", unauthorizedResponse)
                        .addResponses("Forbidden", forbiddenResponse)
                        .addResponses("NotFound", notFoundResponse)
                        .addResponses("InternalServerError", internalServerErrorResponse)
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Bearer token authentication")));
    }

    private ApiResponse createApiResponse(String description, Schema<?> schema) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        "application/json",
                        new MediaType().schema(schema)
                ));
    }
}