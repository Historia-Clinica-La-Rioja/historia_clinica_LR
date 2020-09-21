package net.pladema.snowstorm.configuration;

import lombok.Getter;
import lombok.Setter;
import net.pladema.sgx.restclient.configuration.WSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix="ws.snowstorm")
@Getter
@Setter
public class SnowstormWSConfig extends WSConfig {

    private static final String CONCEPTS = "concepts";

    private static final long DEFAULT_TOKEN_EXPIRATION = -1l;

    //Params
    @Value("#{'${ws.snowstorm.params.preferredOrAcceptableIn:450828004}'.split(',\\s*')}")
    private List<Long> preferredOrAcceptableIn;

    @Value("${ws.snowstorm.params.limit:30}")
    private Integer limit;

    @Value("${ws.snowstorm.params.termActive:true}")
    private Boolean termActive;

    //Headers
    @Value("${ws.snowstorm.auth.language:es-AR;q=0.8,en-GB;q=0.6}")
    private String language;

    private String appId;
    private String appKey;

    //URLS
    @Value("${ws.snowstorm.url.concepts:/MAIN/concepts}")
    private String conceptsUrl;

    private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

    public SnowstormWSConfig(@Value("${ws.snowstorm.url.base:https://snowstorm.msal.gov.ar}") String baseUrl) {
        super(baseUrl);
    }

}
