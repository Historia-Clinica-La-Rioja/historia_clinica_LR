package net.pladema.sgx.swagger.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.Optional;
/**
 * Parse out Spring Security {@link PreAuthorize} annotations and add in operation notes section to convey constraints
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerPreAuthorize implements OperationBuilderPlugin {
    @Override
    public void apply(final OperationContext context) {
        // Look for @PreAuthorize on method, otherwise look on controller
        Optional.ofNullable(context.findAnnotation(PreAuthorize.class)
            .or(context.findControllerAnnotation(PreAuthorize.class))
            .orNull())
            .ifPresent(preAuth -> context.operationBuilder()
                .notes("**Security @PreAuthorize expression:** `" + preAuth.value() + "`"));
    }

    @Override
    public boolean supports(final DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}