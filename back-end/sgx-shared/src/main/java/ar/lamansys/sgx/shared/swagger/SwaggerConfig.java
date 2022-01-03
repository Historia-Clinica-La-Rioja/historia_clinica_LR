package ar.lamansys.sgx.shared.swagger;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${app.apidoc.info.title:HSI API}")
    protected String infoTitle;
    @Value("${app.apidoc.info.description:Historia de Salud Integrada}")
    protected String infoDescription;
    @Value("${app.apidoc.license.name:Apache 2.0}")
    protected String licenseName;
    @Value("${app.apidoc.license.url:http://www.apache.org/licenses/LICENSE-2.0}")
    protected String licenseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(infoTitle)
                        .version("v0.0.1")
                        .description(infoDescription)
                        .license(new License().name(licenseName).url(licenseUrl))
                        .contact(new Contact().name("PLADEMA").url("www.example.com").email("pladema@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

}