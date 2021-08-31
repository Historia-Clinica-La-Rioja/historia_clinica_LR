package ar.lamansys.sgx.auth.apiKey.infrastructure.input.service;

import ar.lamansys.sgx.auth.apiKey.application.login.ApiKeyLogin;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.service.dto.ApiKeyInfoDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyExternalService {

    private final ApiKeyLogin apiKeyLogin;

    public ApiKeyExternalService(ApiKeyLogin apiKeyLogin) {
        this.apiKeyLogin = apiKeyLogin;
    }

    public Optional<ApiKeyInfoDto> login(String apiKey){
        return apiKeyLogin.execute(apiKey)
                .map( apiKeyBo -> new ApiKeyInfoDto(apiKeyBo.getId()));
    }
}
