package net.pladema.hl7.supporting.exchange.services;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ar.lamansys.sgx.shared.restclient.configuration.JWTUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginPayload;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginResponse;

@Service
@Conditional(InteroperabilityCondition.class)
public class BusAuthenticationService extends RestTemplate {

    private final FederarWSConfig configuration;

    public BusAuthenticationService(FederarWSConfig configuration){
        this.configuration=configuration;
    }

    public ResponseEntity<FederarLoginResponse> callLogin() throws RestClientException {
        ResponseEntity<FederarLoginResponse> result;
        try {
            result = exchangePost(configuration.getAuthenticationPath(),
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
        return JWTUtils.generateJWT(configuration.getClaims(), configuration.getSignKey(), configuration.getTokenExpiration().toSeconds());
    }
}
