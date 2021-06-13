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
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.services.BusAuthorizationService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Stream;

@Component
@Conditional(InteroperabilityCondition.class)
public class ServerAuthInterceptor extends AuthorizationInterceptor {

    private static final String BEARER = "Bearer";

    private final BusAuthorizationService authorizationService;

    private final Environment environment;

    public ServerAuthInterceptor(BusAuthorizationService authorizationService,
                                 Environment environment){
        super();
        this.authorizationService=authorizationService;
        this.environment=environment;
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {

        String authHeader = theRequestDetails.getHeader("Authorization");
        if(Stream.of(
                ArrayUtils.addAll(environment.getActiveProfiles(), environment.getDefaultProfiles()))
                .anyMatch(profile -> profile.equals("prod"))) {
            //Production environment
            if (authHeader == null)
                throw new AuthenticationException("Missing or invalid Authorization header");
            if(!authHeader.startsWith(BEARER))
                throw new AuthenticationException("Not authorized — Authorization header does not contain a bearer token");

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
