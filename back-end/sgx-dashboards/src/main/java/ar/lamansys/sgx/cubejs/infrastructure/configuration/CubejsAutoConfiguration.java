package ar.lamansys.sgx.cubejs.infrastructure.configuration;


import java.util.HashMap;
import java.util.Map;

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
    public DashboardStorage dashboardStorageImpl() throws Exception {
        if (!isEnabled()) {
            log.warn("Cubejs dashboards are disabled");
            return new DashboardStorageUnavailableImpl();
        }
        return new DashboardStorageImpl(this);
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