package net.pladema.hsi.extensions.infrastructure.repository;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import net.pladema.hsi.extensions.configuration.RestExtensionWsConfig;
import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.domain.exception.ExtensionException;
import net.pladema.hsi.extensions.domain.exception.ExtensionExceptionEnum;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import org.springframework.web.client.RestClientException;

public class RestExtensionService implements ExtensionService {

    private final RestClient restClient;
    private final RestExtensionWsConfig restExtensionWsConfig;

    public RestExtensionService(RestTemplateSSL restTemplateSSL, RestExtensionWsConfig wsConfig) {
        this.restClient = new RestClient(restTemplateSSL, wsConfig);
        this.restExtensionWsConfig = wsConfig;
    }

    @Override
    public UIMenuItemDto[] getSystemMenu() throws ExtensionException {
        try {
            return restClient.exchangeGet( "system", UIMenuItemDto[].class).getBody();
        } catch (RestClientException e) {
            throw new ExtensionException(ExtensionExceptionEnum.TIMEOUT, "Tiempo agotado");
        }
    }

    @Override
    public UIPageDto getSystemPage(String menuId) {
        return restClient.exchangeGet("system/page/"+menuId, UIPageDto.class).getBody();
    }

    @Override
    public UIMenuItemDto[] getInstitutionMenu(Integer institutionId) {
        return restClient.exchangeGet("institution/"+institutionId, UIMenuItemDto[].class).getBody();
    }

    @Override
    public UIPageDto getInstitutionPage(Integer institutionId, String menuId) {
        return restClient.exchangeGet("institution/"+institutionId+"/page/"+menuId, UIPageDto.class).getBody();
    }

    @Override
    public UIMenuItemDto[] getPatientMenu(Integer patientId) {
        return restClient.exchangeGet("patient/"+patientId, UIMenuItemDto[].class).getBody();
    }

    @Override
    public UIPageDto getPatientPage(Integer patientId, String menuId) {
        return restClient.exchangeGet("patient/"+patientId+"/page/"+menuId, UIPageDto.class).getBody();
    }
}
