package net.pladema.hl7.supporting.security;

import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.services.BusAuthenticationService;
import net.pladema.hl7.supporting.exchange.services.federar.FederarLoginResponse;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Conditional(InteroperabilityCondition.class)
public class ClientAuthInterceptor extends BearerTokenAuthInterceptor {

    private final BusAuthenticationService authenticationService;

    private final Environment environment;

    public ClientAuthInterceptor(BusAuthenticationService authenticationService,
                                 Environment environment){
        super();
        this.authenticationService=authenticationService;
        this.environment=environment;
    }

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        String token = "jwt";
        try {
            FederarLoginResponse body = authenticationService.callLogin().getBody();
            if(body != null)
                token = body.getToken();
        }
        catch(NullPointerException | RestClientException e){
            e.printStackTrace();
        }
        theRequest.addHeader("Authorization", "Bearer " + token);
    }
}
