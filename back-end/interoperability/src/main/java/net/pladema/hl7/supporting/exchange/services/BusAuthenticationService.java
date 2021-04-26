package net.pladema.hl7.supporting.exchange.services;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginPayload;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginResponse;
import net.pladema.hl7.supporting.exchange.services.federar.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class BusAuthenticationService extends RestTemplate {

    private final FederarConfig configuration;

    @Value("${ws.federar.url.login}")
    private String relativeUrl;

    public BusAuthenticationService(FederarConfig configuration){
        this.configuration=configuration;
    }

    public ResponseEntity<FederarLoginResponse> callLogin() throws RestClientException {
        ResponseEntity<FederarLoginResponse> result;
        try {
            result = exchangePost(relativeUrl,
                    new FederarLoginPayload(configuration.getGrantType(), configuration.getScope(),
                            configuration.getClientAssertionType(), generateClientAssertion()));
        } catch (RestClientException e) {
            throw new AuthenticationException("Invalid access token: " + e.getMessage() ) ;
        }
        return result;
    }

    private ResponseEntity<FederarLoginResponse> exchangePost(String relUrl,
                                                                     FederarLoginPayload requestBody) {
        String fullUrl = configuration.getAbsoluteURL(relUrl);
        HttpEntity<FederarLoginPayload> entity = new HttpEntity<>(requestBody, getHeaders());
        return exchange(fullUrl, HttpMethod.POST, entity, FederarLoginResponse.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }

    private String generateClientAssertion() {
        return JWTUtils.generateJWT(configuration.getClaims(), configuration.getSignKey(), (int) configuration.getTokenExpiration());
    }
}
