package net.pladema.hl7.supporting.security;

import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import net.pladema.hl7.supporting.exchange.services.BusAuthenticationService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
public class ClientAuthInterceptor extends BearerTokenAuthInterceptor {

    private final BusAuthenticationService authenticationService;

    public ClientAuthInterceptor(BusAuthenticationService authenticationService){
        super();
        this.authenticationService=authenticationService;
    }

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        String token = null;
        try {
            token = authenticationService.callLogin().getBody().getToken();
        }
        catch(NullPointerException | RestClientException e){
            token = "parapatintin";
        }
        theRequest.addHeader("Authorization", "Bearer " + token);
    }
}
