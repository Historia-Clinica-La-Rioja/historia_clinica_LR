package ar.lamansys.sgx.auth.jwt.infrastructure.input.service;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.SgxUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationExternalServiceImpl implements AuthenticationExternalService {

    private final SgxUserDetailsService sgxUserDetailsService;

    public AuthenticationExternalServiceImpl(SgxUserDetailsService sgxUserDetailsService) {
        this.sgxUserDetailsService = sgxUserDetailsService;
    }

    @Override
    public Optional<Authentication> getAppAuthentication(String username){
        return sgxUserDetailsService.loadSgxUserByUsername(username)
                .map(sgxUserDetails -> new UsernamePasswordAuthenticationToken(
                        sgxUserDetailsService.loadUserByUsername(username),
                        ""
                ));
    }
}
