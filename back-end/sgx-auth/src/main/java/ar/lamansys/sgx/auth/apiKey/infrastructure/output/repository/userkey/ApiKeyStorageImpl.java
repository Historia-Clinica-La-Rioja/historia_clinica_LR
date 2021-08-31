package ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey;

import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyBo;
import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyStorage;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyStorageImpl implements ApiKeyStorage {

    private final UserKeyRepository userKeyRepository;

    public ApiKeyStorageImpl(UserKeyRepository userKeyRepository) {
        this.userKeyRepository = userKeyRepository;
    }

    @Override
    public ApiKeyBo getApiKeyInfo(String apiKey) {
        return userKeyRepository.getUserKeyByKey(apiKey)
                .map(userKey -> new ApiKeyBo(userKey.getUserId()))
                .orElse(null);
    }
}
