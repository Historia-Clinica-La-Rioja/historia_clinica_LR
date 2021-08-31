package ar.lamansys.sgx.auth.apiKey.domain;


public interface ApiKeyStorage {

    ApiKeyBo getApiKeyInfo(String apiKey);
}
