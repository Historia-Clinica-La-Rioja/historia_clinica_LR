package ar.lamansys.pac;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import ar.lamansys.pac.infrastructure.input.rest.interceptors.AuthInterceptor;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.auth.PreFlightInterceptor;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.auth.TokenJWTInterceptor;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.auth.TokenUUIDInterceptor;
import ar.lamansys.pac.infrastructure.output.inmemory.repository.StudyPermissionsUUIDRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Configuration
@Getter
@AllArgsConstructor
public class AuthChainConfiguration {

    @AllArgsConstructor
    public static class AuthChainInterceptor implements HandlerInterceptor {

        private AuthInterceptor firstAuthInterceptor;
        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
            return firstAuthInterceptor.process(request, response);
        }
    }

    @Bean("authChainInterceptor")
    @Primary
    public HandlerInterceptor AuthChainInterceptor(PreFlightInterceptor preFlightInterceptor) {
        return new AuthChainInterceptor(preFlightInterceptor);
    }

    @Bean
    public PreFlightInterceptor PreFlightInterceptor(TokenJWTInterceptor tokenJWTInterceptor) {
        return new PreFlightInterceptor(tokenJWTInterceptor);
    }
    @Bean("jwtInterceptorValidatorHSI")
	@ConditionalOnProperty(name = "app.imagenetwork.can-verify-token", havingValue = "false")
    public TokenJWTInterceptor TokenJWTInterceptor(TokenUUIDInterceptor uuidInterceptor, RestTemplate restTemplate) {
        return new TokenJWTInterceptor(uuidInterceptor, restTemplate);
    }

    @Bean("jwtInterceptorValidator")
	@ConditionalOnProperty(name = "app.imagenetwork.can-verify-token", havingValue = "true")
    public TokenJWTInterceptor TokenJWTInterceptor(TokenUUIDInterceptor uuidInterceptor, @Value("${app.imagenetwork.token.secret}") String secret) {
        return new TokenJWTInterceptor(uuidInterceptor, secret);
    }

    @Bean
    public TokenUUIDInterceptor TokenUUIDInterceptor(RestTemplate restTemplate) {
        return new TokenUUIDInterceptor((request, response) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }, new StudyPermissionsUUIDRepository(), restTemplate);
    }

}

