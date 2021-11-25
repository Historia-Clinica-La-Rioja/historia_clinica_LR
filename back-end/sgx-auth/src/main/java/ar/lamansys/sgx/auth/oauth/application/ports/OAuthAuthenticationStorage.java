package ar.lamansys.sgx.auth.oauth.application.ports;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface OAuthAuthenticationStorage {

    Optional<Authentication> getAppAuthentication(String username);

}
