package ar.lamansys.sgx.auth.oauth.infrastructure.output.config;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OAuthWSConfig extends WSConfig {

    private static final String REALM_NAME_PLACEHOLDER = "REALM_NAME";

    private final long tokenExpiration = -1L;

    @Value("${ws.oauth.realm:}")
    private String realmName;

    //URLS
    @Value("${ws.oauth.url.userinfo:/auth/realms/REALM_NAME/protocol/openid-connect/userinfo}")
    private String userInfo;

    public String getUserInfo() {
        String userInfoUrl = userInfo;
        return userInfoUrl.replaceFirst(REALM_NAME_PLACEHOLDER, realmName);
    }

    public OAuthWSConfig(@Value("${ws.oauth.url.base:}") String baseUrl) {
        super(baseUrl, false);
    }

}
