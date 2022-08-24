package ar.lamansys.sgx.auth.oauth.infrastructure.output;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.oauth.application.ports.OAuthTokenStorage;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.auth.oauth.infrastructure.output.dto.OAuthRefreshTokenResponse;
import ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration.OAuthLoginInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OAuthTokenStorageImpl extends RestClient implements OAuthTokenStorage {

    private final OAuthWSConfig oAuthWSConfig;

    public OAuthTokenStorageImpl(
			HttpClientConfiguration configuration,
			OAuthWSConfig oAuthWSConfig
	) throws Exception {
        super(getRestTemplateSSL(configuration), oAuthWSConfig);
        this.oAuthWSConfig = oAuthWSConfig;
    }

    private static RestTemplateSSL getRestTemplateSSL(
			HttpClientConfiguration configuration
	) throws Exception {
        var restTemplate = new RestTemplateSSL(configuration);
        restTemplate.getInterceptors().add(0, new OAuthLoginInterceptor()); // adds the interceptor in the first position
        return restTemplate;
    }

    @Override
    public Optional<JWTokenBo> refreshToken(String refreshToken) {
        log.debug("Input parameter -> refreshToken {}", refreshToken);
        String url = oAuthWSConfig.getFetchAccessToken();
        try {
            ResponseEntity<OAuthRefreshTokenResponse> response = exchangePost(url, mapToRefreshTokenPayload(refreshToken), OAuthRefreshTokenResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("Token refresh successful in OAuth Server");
                OAuthRefreshTokenResponse responseBody = response.getBody();
                return Optional.of(new JWTokenBo(responseBody.getAccessToken(),responseBody.getRefreshToken()));
            }
        } catch (Exception e) {
            log.debug("Error refreshing token in OAuth Server");
        }
        return Optional.empty();
    }

    private String mapToRefreshTokenPayload(String refreshToken) {
        return "refresh_token=" + refreshToken +
                "&client_id=" + oAuthWSConfig.getClientId() +
                "&grant_type=refresh_token";
    }

}
