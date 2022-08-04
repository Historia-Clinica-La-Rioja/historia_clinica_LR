package ar.lamansys.sgx.cubejs.application.dashboardinfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import ar.lamansys.sgx.cubejs.CubejsAutoConfiguration;
import ar.lamansys.sgx.cubejs.application.dashboardinfo.excepciones.DashboardInfoException;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageUnavailableImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorageEmptyImpl;
import ar.lamansys.sgx.shared.proxy.reverse.ReverseProxy;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;

@ExtendWith(MockitoExtension.class)
class DashboardInfoServiceImplTest {
    private DashboardInfoService dashboardInfoService;

    @Mock
    private ReverseProxy reverseProxy;

    @Mock
    private CubejsAutoConfiguration cubejsAutoConfiguration;

    @BeforeEach
    public void setUp() {
    }


    @Test
    @DisplayName("Dashboardinfo success with enable storage")
    @Disabled
    void dashboardinfo_enable_storage_success() throws Exception {
        when(reverseProxy.getAsString(any(), any())).thenReturn(ResponseEntity.of(Optional.empty()));
        when(cubejsAutoConfiguration.getApiUrl()).thenReturn("http://localhost:4000/cubejs-api");
        when(cubejsAutoConfiguration.getHeaders()).thenReturn(new HashMap<>());
        dashboardInfoService = new DashboardInfoServiceImpl(
				new DashboardStorageImpl(cubejsAutoConfiguration, new UserPermissionStorageEmptyImpl(),
						newHttpClientConfiguration(),
						"SECRET", "AUTHORIZATION", Duration.ofDays(2)));
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("PRUEBA",new String[]{"Prueba2"});
        var result = dashboardInfoService.execute("TEST",parameterMap);
        assertThat(result)
                .isNotNull();
    }

	private HttpClientConfiguration newHttpClientConfiguration() {
		return new HttpClientConfiguration(null, 1500, false);
	}

	@Test
    @DisplayName("Dashboardinfo faild with disable storage")
    void dashboardinfo_disable_storage_exception() {

        dashboardInfoService = new DashboardInfoServiceImpl(new DashboardStorageUnavailableImpl());
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("PRUEBA",new String[]{"Prueba2"});
        Exception exception = Assertions.assertThrows(DashboardInfoException.class, () ->
                dashboardInfoService.execute("TEST",parameterMap)
        );
        String expectedMessage = "La url de la api no esta definida";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNotNull().isEqualTo(expectedMessage);
    }

}