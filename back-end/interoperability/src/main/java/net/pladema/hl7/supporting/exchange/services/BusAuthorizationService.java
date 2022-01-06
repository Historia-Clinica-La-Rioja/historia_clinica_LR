package net.pladema.hl7.supporting.exchange.services;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Conditional(InteroperabilityCondition.class)
public class BusAuthorizationService extends RestTemplate {

    private final FederarWSConfig configuration;

    public BusAuthorizationService(FederarWSConfig configuration){
        super();
        this.configuration=configuration;
    }

    @Getter
    @Setter
    @ToString
    public static class AuthorizationPayload {

        private String accessToken;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public AuthorizationPayload(@JsonProperty("accessToken") String accessToken) {
            this.accessToken = accessToken;
        }

        public String getToken() {
            return accessToken;
        }
    }

    public ResponseEntity<FederarLoginResponse> validate(String accessToken) throws RestClientException {
        accessToken = accessToken.replace("Bearer", "").trim();
        ResponseEntity<FederarLoginResponse> result;
        try {
            result = exchangePost(configuration.getAuthorizationPath(), new AuthorizationPayload(accessToken));
        } catch (RestClientException e) {
            throw new AuthenticationException("Invalid access token: " + e.getMessage() ) ;
        }
        return result;
    }

    private ResponseEntity<FederarLoginResponse> exchangePost(String relUrl,
                                                              AuthorizationPayload requestBody) {
        String fullUrl = configuration.getAbsoluteURL(relUrl);
        HttpEntity<AuthorizationPayload> entity = new HttpEntity<>(requestBody, getHeaders());
        return exchange(fullUrl, HttpMethod.POST, entity, FederarLoginResponse.class);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
