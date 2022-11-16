package ar.lamansys.sgx.cubejs;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageUnavailableImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorage;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
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
	private String proxy;

    private Map<String, String> headers = new HashMap<>();

    public boolean isEnabled(boolean featureEnabled) {
        return apiUrl != null && !apiUrl.isBlank() && featureEnabled;
    }

    @Bean
    public DashboardStorage dashboardStorageImpl(
			UserPermissionStorage userPermissionStorage,
			HttpClientConfiguration httpClientConfiguration,
			@Value("${app.gateway.cubejs.token.secret}") String secret,
			@Value("${app.gateway.cubejs.token.header:Authorization}") String cubeTokenHeader,
			@Value("${app.gateway.cubejs.token.expiration:20d}") Duration tokenExpiration
	) throws Exception {
        if (!isEnabled(true)) {
            log.warn("Cubejs dashboards are disabled");
            return new DashboardStorageUnavailableImpl();
        }
        return new DashboardStorageImpl(
				this,
				userPermissionStorage,
				httpClientConfiguration.withProxy(this.proxy),
				secret,
				cubeTokenHeader,
				tokenExpiration
		);
    }

    @Bean
    public InstitutionMenuExtensionPlugin referencias() {
        var result = isEnabled(true) ? InstitutionMenuExtensionPluginBuilder.fromResources("references") : null;
        if (result != null) {
            log.info("Cubejs InstitutionMenuExtensionPlugin {}", result.menu());
        } else {
            log.warn("Cubejs InstitutionMenuExtensionPlugin not defined");
        }
        return result;
    }

	@Bean
	public InstitutionMenuExtensionPlugin reportesEstadisticos(FeatureFlagsService featureFlagsService){
    	var result = isEnabled(featureFlagsService.isOn(AppFeature.HABILITAR_REPORTES_ESTADISTICOS)) ? InstitutionMenuExtensionPluginBuilder.fromResources("reportesEstadisticos" ) : null;
		if (result != null) {
			log.info("Cubejs InstitutionMenuExtensionPlugin {}", result.menu());
		} else {
			log.warn("Cubejs InstitutionMenuExtensionPlugin not defined");
		}
		return result;
	}

}