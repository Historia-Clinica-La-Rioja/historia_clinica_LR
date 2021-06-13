package ar.lamansys.sgx.cubejs.infrastructure.configuration;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageUnavailableImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public DashboardStorage DashboardStorageImpl() throws Exception {
        if (apiUrl == null || apiUrl.isBlank())
            return new DashboardStorageUnavailableImpl();
        return new DashboardStorageImpl(this);
    }
}