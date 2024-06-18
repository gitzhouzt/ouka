package com.cbs.oukasystem.config;

import io.swagger.v3.oas.annotations.Operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger3Config {

        @Bean
        public Docket createRestApi() {
                return new Docket(DocumentationType.OAS_30)
                                .apiInfo(apiInfo())
                                .enable(true)
                                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme.JWT_BEARER_BUILDER
                                                .name("JWT")
                                                .build()))
                                .securityContexts(Collections.singletonList(SecurityContext.builder()
                                                .securityReferences(
                                                                Collections.singletonList(SecurityReference.builder()
                                                                                .scopes(new AuthorizationScope[0])
                                                                                .reference("JWT")
                                                                                .build()))
                                                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                                                .build()))
                                .select()
                                .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
                                .paths(PathSelectors.any())
                                .build();
        }

        private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(
                        Arrays.asList("application/json", "application/xml", "application/x-www-form-urlencoded"));

        private ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                                .title("Swagger3 API Doc")
                                .description("Oukasystem API Application Version: 1.0, Spring Boot Version: 2.7.0")
                                .contact(new Contact("Saki", null, "hcc.sak@gmail.com"))
                                .version("1.0")
                                .build();
        }
}