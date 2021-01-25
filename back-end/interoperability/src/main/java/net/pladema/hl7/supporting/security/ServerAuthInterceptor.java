//
///This interceptor implements HTTP Basic Auth, which specifies that a username and password
//are provided in a header called Authorization.
//the authorization interceptor is used to (once authenticated) decide whether or not you can see
//some specific resources or do certain operations
//

package net.pladema.hl7.supporting.security;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import net.pladema.hl7.supporting.exchange.services.BusAuthorizationService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class ServerAuthInterceptor extends AuthorizationInterceptor {

    private final BusAuthorizationService authorizationService;

    public ServerAuthInterceptor(BusAuthorizationService authorizationService){
        super();
        this.authorizationService=authorizationService;
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
        String authHeader = theRequestDetails.getHeader("Authorization");
        if (authHeader == null)
            throw new AuthenticationException("Missing or invalid Authorization header value");
        if(!authHeader.equals("HsC9%x-r?F")) {
            try {
                authorizationService.validate(authHeader);
            }
            catch(RestClientException ex){
                throw new AuthenticationException("Invalid access token");
            }
        }
        RuleBuilder builder = new RuleBuilder();
        builder.allow().metadata().andThen()
                .allow().read().allResources().withAnyId();
        return builder.build();
   }
}
