package ar.lamansys.sgx.auth.oauth.infrastructure.output.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;

@Component
@Getter
public class OAuthWSConfig extends WSConfig {

    private static final String REALM_NAME_PLACEHOLDER = "REALM_NAME";

	@Value("${ws.oauth.token-expiration:18s}")
    private Duration tokenExpiration;

    @Value("${ws.oauth.enabled:false}")
    private boolean enabled;

    @Value("${ws.oauth.realm:}")
    private String realmName;

    @Value("${ws.oauth.client-id:}")
    private String clientId;

    @Value("${ws.oauth.user-admin.username:}")
    private String userAdminUsername;

    @Value("${ws.oauth.user-admin.password:}")
    private String userAdminPassword;

    //URLS
    @Value("${ws.oauth.url.userinfo:/auth/realms/REALM_NAME/protocol/openid-connect/userinfo}")
    private String userInfo;

    @Value("${ws.oauth.url.accesstoken:/auth/realms/REALM_NAME/protocol/openid-connect/token}")
    private String fetchAccessToken;

    @Value("${ws.oauth.url.logout:/auth/realms/REALM_NAME/protocol/openid-connect/logout}")
    private String logout;

    @Value("${ws.oauth.url.createuser:/auth/admin/realms/REALM_NAME/users}")
    private String createUser;

    @Value("${ws.oauth.url.issuer:}")
    private String issuer;

    public String getUserInfo() {
        String userInfoUrl = userInfo;
        return userInfoUrl.replaceFirst(REALM_NAME_PLACEHOLDER, realmName);
    }

    public String getFetchAccessToken() {
        String accessTokenUrl = fetchAccessToken;
        return accessTokenUrl.replaceFirst(REALM_NAME_PLACEHOLDER, realmName);
    }

    public String getCreateUser() {
        String createUserUrl = createUser;
        return createUserUrl.replaceFirst(REALM_NAME_PLACEHOLDER, realmName);
    }

    public String getLogout() {
        String logoutUrl = logout;
        return logoutUrl.replaceFirst(REALM_NAME_PLACEHOLDER, realmName);
    }

    public OAuthWSConfig(@Value("${ws.oauth.url.base:}") String baseUrl) {
        super(baseUrl, false);
    }

    public String getUserAdminUsername() {
        return userAdminUsername;
    }

    public String getUserAdminPassword() {
        return userAdminPassword;
    }

    public String getClientId() {
        return clientId;
    }
}
