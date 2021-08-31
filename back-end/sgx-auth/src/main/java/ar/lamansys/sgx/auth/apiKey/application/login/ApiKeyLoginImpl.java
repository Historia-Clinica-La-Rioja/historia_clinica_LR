package ar.lamansys.sgx.auth.apiKey.application.login;

import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyBo;
import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyLoginImpl implements ApiKeyLogin {

    private final Logger logger;

    private final ApiKeyStorage apiKeyStorage;

    public ApiKeyLoginImpl(ApiKeyStorage apiKeyStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.apiKeyStorage = apiKeyStorage;
    }

    @Override
    public Optional<ApiKeyBo> execute(String apiKey) {
        logger.debug("Login by api-key");
        return Optional.ofNullable(apiKeyStorage.getApiKeyInfo(apiKey));
    }

}
