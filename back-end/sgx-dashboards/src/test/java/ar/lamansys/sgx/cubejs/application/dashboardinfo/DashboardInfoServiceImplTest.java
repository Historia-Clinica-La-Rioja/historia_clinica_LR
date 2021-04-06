package ar.lamansys.sgx.cubejs.application.dashboardinfo;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.excepciones.DashboardInfoException;
import ar.lamansys.sgx.cubejs.infrastructure.configuration.CubejsAutoConfiguration;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageUnavailableImpl;
import ar.lamansys.sgx.cubejs.infrastructure.repository.DashboardStorageImpl;
import ar.lamansys.sgx.proxy.reverse.ReverseProxy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DashboardInfoServiceImplTest {
    private DashboardInfoService dashboardInfoService;

    @MockBean
    private ReverseProxy reverseProxy;

    @MockBean
    private CubejsAutoConfiguration cubejsAutoConfiguration;

    @Before
    public void setUp() {
    }


    @Test
    @DisplayName("Dashboardinfo success with enable storage")
    @Ignore
    public void dashboardinfo_enable_storage_success() throws Exception {
        when(reverseProxy.getAsString(any(), any())).thenReturn(ResponseEntity.of(Optional.empty()));
        when(cubejsAutoConfiguration.getApiUrl()).thenReturn("http://localhost:4000/cubejs-api");
        when(cubejsAutoConfiguration.getHeaders()).thenReturn(new HashMap<>());
        dashboardInfoService = new DashboardInfoServiceImpl(new DashboardStorageImpl(cubejsAutoConfiguration));
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("PRUEBA",new String[]{"Prueba2"});
        var result = dashboardInfoService.execute("TEST",parameterMap);
        assertThat(result)
                .isNotNull();
    }

    @Test
    @DisplayName("Dashboardinfo faild with disable storage")
    public void dashboardinfo_disable_storage_exception() {

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