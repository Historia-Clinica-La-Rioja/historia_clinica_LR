package ar.lamansys.sgx.auth.oauth.infrastructure.output;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import ar.lamansys.sgx.auth.oauth.application.ports.OAuthAuthenticationStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthAuthenticationStorageImpl implements OAuthAuthenticationStorage {

    private final AuthenticationExternalService authenticationExternalService;

    @Override
    public Optional<Authentication> getAppAuthentication(String username) {
        log.debug("Input parameter -> username {}", username);
        Optional<Authentication> result = authenticationExternalService.getAppAuthentication(username);
        log.debug("Output -> {}", result);
        return result;
    }

}
