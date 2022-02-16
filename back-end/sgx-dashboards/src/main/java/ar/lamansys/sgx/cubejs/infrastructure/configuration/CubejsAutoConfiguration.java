package ar.lamansys.sgx.cubejs.infrastructure.configuration;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageUnavailableImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.extensions.configuration.plugins.InstitutionMenuExtensionPlugin;
import net.pladema.hsi.extensions.configuration.plugins.InstitutionMenuExtensionPluginBuilder;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app.gateway.cubejs")
@ComponentScan(basePackages = "ar.lamansys.sgx.cubejs")
@PropertySource(value = "classpath:dashboards.properties", ignoreResourceNotFound = true)
public class CubejsAutoConfiguration {
    private String apiUrl;

    private Map<String, String> headers = new HashMap<>();

    public boolean isEnabled() {
        return apiUrl != null && !apiUrl.isBlank();
    }

    @Bean
    public DashboardStorage dashboardStorageImpl(UserPermissionStorage userPermissionStorage,
								@Value("${app.gateway.cubejs.token.secret}") String secret,
								@Value("${app.gateway.cubejs.token.header:Authorization}") String cubeTokenHeader,
								@Value("${app.gateway.cubejs.token.expiration:20d}") Duration tokenExpiration) throws Exception {
        if (!isEnabled()) {
            log.warn("Cubejs dashboards are disabled");
            return new DashboardStorageUnavailableImpl();
        }
        return new DashboardStorageImpl(this, userPermissionStorage, secret, cubeTokenHeader, tokenExpiration);
    }

    @Bean
    public InstitutionMenuExtensionPlugin dashboardsExtensionPlugin() {
        var result = isEnabled() ? InstitutionMenuExtensionPluginBuilder.fromResources("tableros") : null;
        if (result != null) {
            log.info("Cubejs InstitutionMenuExtensionPlugin {}", result.menu());
        } else {
            log.warn("Cubejs InstitutionMenuExtensionPlugin not defined");
        }
        return result;
    }

}