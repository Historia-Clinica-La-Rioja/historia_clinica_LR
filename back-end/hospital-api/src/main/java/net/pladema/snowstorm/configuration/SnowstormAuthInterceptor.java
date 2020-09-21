package net.pladema.snowstorm.configuration;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

import net.pladema.sgx.restclient.configuration.TokenHolder;
import net.pladema.sgx.restclient.configuration.interceptors.AuthInterceptor;
import net.pladema.snowstorm.services.SnowstormAuthService;
import net.pladema.snowstorm.services.domain.SnowstormLoginResponse;

@Component
public class SnowstormAuthInterceptor extends AuthInterceptor<SnowstormLoginResponse, SnowstormAuthService> {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String APP_KEY = "app_key";
    private static final String APP_ID = "app_id";

    private SnowstormWSConfig snowstormWSConfig;

    public SnowstormAuthInterceptor(SnowstormAuthService authService, SnowstormWSConfig snowstormWSConfig) {
        super(authService, new TokenHolder(snowstormWSConfig.getTokenExpiration()));
        this.snowstormWSConfig = snowstormWSConfig;
    }

    @Override
    protected void addAuthHeaders(HttpHeaders headers) {
        headers.add(ACCEPT_LANGUAGE, snowstormWSConfig.getLanguage());

        if (snowstormWSConfig.getAppId() != null && snowstormWSConfig.getAppKey() != null) {
            headers.add(APP_ID, snowstormWSConfig.getAppId());
            headers.add(APP_KEY, snowstormWSConfig.getAppKey());
        }
    }
}
