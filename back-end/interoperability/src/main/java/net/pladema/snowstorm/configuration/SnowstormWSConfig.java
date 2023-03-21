package net.pladema.snowstorm.configuration;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="ws.snowstorm")
@Getter
@Setter
public class SnowstormWSConfig extends WSConfig {

    private static final String CONCEPTS = "concepts";

    private static final Duration DEFAULT_TOKEN_EXPIRATION = Duration.ofSeconds(-1L);

    //Params
    @Value("#{'${ws.snowstorm.params.preferredOrAcceptableIn:450828004}'.split(',\\s*')}")
    private List<Long> preferredOrAcceptableIn;

    @Value("${ws.snowstorm.params.limit:30}")
    private Integer conceptsLimit;

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

    @Value("${ws.snowstorm.url.concept.browser:browser/MAIN/concepts}")
    private String browserConceptUrl;

    @Value("${ws.snowstorm.url.refsetMembers:/MAIN/members}")
    private String refsetMembersUrl;

    private Duration tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

    public SnowstormWSConfig(@Value("${ws.snowstorm.url.base:https://snowstorm.msal.gob.ar}") String baseUrl) {
        super(baseUrl, false);
    }

}
