//
///This interceptor implements HTTP Basic Auth, which specifies that a username and password
//are provided in a header called Authorization.
//the authorization interceptor is used to (once authenticated) decide whether or not you can see
//some specific resources or do certain operations
//

package net.pladema.hl7.supporting.security;

import ar.lamansys.sgx.shared.fhir.application.port.FhirPermissionsPort;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ServerAuthInterceptor extends AuthorizationInterceptor {

    private static final String BEARER = "Bearer";
    public static final String HEADER = "Authorization";

    private final BusAuthorizationService authorizationService;

    private final Environment environment;

    private final FhirPermissionsPort fhirPermissionsPort;

    public ServerAuthInterceptor(
    	BusAuthorizationService authorizationService,
		Environment environment,
		FhirPermissionsPort fhirPermissionsPort)
	{
        super();
        this.authorizationService = authorizationService;
        this.environment = environment;
        this.fhirPermissionsPort = fhirPermissionsPort;
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		RuleBuilder builder = new RuleBuilder();
		//The user has at least access to a fhir resource
		//Each endpoint must check the permissions on its own
		if (fhirPermissionsPort.shouldEnableFhirResources()) {
			builder.allow().metadata().andThen()
					.allow().read().allResources().withAnyId().andThen()
					.allow().write().allResources().withAnyId();
		}
		else {
			builder.denyAll();
		}
        return builder.build();
   }

	public void validToken(String authHeader) {
		if(isProd()) {
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
	}

	private boolean isProd() {
		return Stream.
		of(ArrayUtils.addAll(environment.getActiveProfiles(), environment.getDefaultProfiles()))
		.anyMatch(profile -> profile.equals("prod"));
	}
}
