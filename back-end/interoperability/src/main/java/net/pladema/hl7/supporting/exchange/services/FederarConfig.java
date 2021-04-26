package net.pladema.hl7.supporting.exchange.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix="ws.federar")
@Getter
@Setter
public class FederarConfig {

    private String baseUrl;

    private static final String ISS = "iss";
    private static final String SUB = "sub";
    private static final String AUD = "aud";
    private static final String NAME = "name";
    private static final String ROLE = "role";
    private static final String IDENT = "ident";
    private static final String GRANT_TYPE="grantType";
    private static final String SCOPE="scope";
    private static final String CLIENT_ASSERTION_TYPE="clientAssertionType";
    private static final String SIGN_KEY = "signKey";

    //URL
    private static final String TOKEN_VALID="validateToken";
    private static final String SEARCH_LOCAL_ID ="localIdSearch";

    private static final long DEFAULT_TOKEN_EXPIRATION = 180l;
    private static final String FEDERATE ="federate";

    private Map<String, String> url;
    private Map<String, String> claims;
    private Map<String, String> auth;

    private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;


    public FederarConfig(@Value("${ws.federar.url.base}") String baseUrl) {
        this.baseUrl=baseUrl;
        url = new HashMap<>();
    }

    public String getIss() {
        return claims.get(ISS);
    }

    public String getDomain() {
        return claims.get(ISS);
    }

    public String getSub() {
        return claims.get(SUB);
    }

    public String getAud() {
        return claims.get(AUD);
    }

    public String getName() {
        return claims.get(NAME);
    }

    public String getIdent() {
        return claims.get(IDENT);
    }

    public String getRole() {
        return claims.get(ROLE);
    }

    public String getGrantType() {
        return auth.get(GRANT_TYPE);
    }

    public String getSignKey() {
        return auth.get(SIGN_KEY);
    }

    public String getScope() {
        return auth.get(SCOPE);
    }

    public String getClientAssertionType() {
        return auth.get(CLIENT_ASSERTION_TYPE);
    }

    public String getTokenValidationURL() {
        return url.get(TOKEN_VALID);
    }

    public String getLocalSearchIdUrl() {
        return url.get(SEARCH_LOCAL_ID);
    }

    public String getFederateUrl() {
        return url.get(FEDERATE);
    }

    public String getAbsoluteURL(String relURL) {
        Assert.notNull(relURL, "WS relative URL can not be null");
        return joinURL(this.baseUrl, relURL);
    }

    public static String joinURL(String prefix, String suffix) {
        return String.format("%s/%s", prefix.replaceAll("/$", ""), suffix.replaceAll("^/", ""));
    }
}
