package net.pladema.swagger.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	
	@Value("${token.header}")
	private String tokenHeader;
	
    @Bean
    public Docket api() { 
    	return new Docket(DocumentationType.SWAGGER_2)  
            .apiInfo(apiInfo())
            .pathMapping("/")
            .apiInfo(ApiInfo.DEFAULT)
            .forCodeGeneration(true)
            .genericModelSubstitutes(ResponseEntity.class)
            .securityContexts(Lists.newArrayList(securityContext()))
            .securitySchemes(Lists.newArrayList(apiKey()))
            .useDefaultResponseMessages(false)
            .select()
            .paths(PathSelectors.ant("/**"))
            .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
          "My REST API", 
          "Some custom description of API.", 
          "API TOS", 
          "Terms of service", 
          new Contact("PLADEMA", "www.example.com", "pladema@gmail.com"), 
          "License of API", "API license URL", Collections.emptyList());
    }
    
    private ApiKey apiKey() {
        return new ApiKey("JWT", tokenHeader, "header");
    }
    
    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }
    
    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                		new AlternateTypeRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
                );
            }
        };
    }
    
    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class, "page"),
                        property(Integer.class, "size"),
                        property(String.class, "sort")
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }
    
}