package ar.lamansys.sgx.auth.apiKey.application.login;

import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyBo;

import java.util.Optional;

public interface ApiKeyLogin {

    Optional<ApiKeyBo> execute(String apiKey);
}
