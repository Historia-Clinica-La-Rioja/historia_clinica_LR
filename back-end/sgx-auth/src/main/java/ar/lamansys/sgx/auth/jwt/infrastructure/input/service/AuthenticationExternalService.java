package ar.lamansys.sgx.auth.jwt.infrastructure.input.service;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationExternalService {

    Optional<Authentication> getAppAuthentication(String username);
}
