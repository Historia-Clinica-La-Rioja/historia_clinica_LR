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
import net.pladema.hsi.extensions.configuration.plugins.SystemMenuExtensionPlugin;
import net.pladema.hsi.extensions.configuration.plugins.SystemMenuExtensionPluginBuilder;

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

    @Bean
    public DashboardStorage dashboardStorageImpl() throws Exception {
        if (apiUrl == null || apiUrl.isBlank())
            return new DashboardStorageUnavailableImpl();
        return new DashboardStorageImpl(this);
    }

    @Bean
    public SystemMenuExtensionPlugin dashboardsExtensionPlugin() {
        return SystemMenuExtensionPluginBuilder.fromResources("tableros");
    }

}