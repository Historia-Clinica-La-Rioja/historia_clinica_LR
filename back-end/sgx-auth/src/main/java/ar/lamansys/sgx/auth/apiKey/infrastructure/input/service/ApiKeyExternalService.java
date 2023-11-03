package ar.lamansys.sgx.auth.apiKey.infrastructure.input.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.apiKey.application.login.ApiKeyLogin;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.service.dto.ApiKeyInfoDto;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.SgxUserDetailsService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ApiKeyExternalService {

    private final ApiKeyLogin apiKeyLogin;
	private final SgxUserDetailsService sgxUserDetailsService;

    public Optional<ApiKeyInfoDto> login(String apiKey){
        return apiKeyLogin.execute(apiKey)
                .map( apiKeyBo -> new ApiKeyInfoDto(apiKeyBo.getId()));
    }
}
