package ar.lamansys.sgx.auth.oauth.application;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.oauth.application.ports.OAuthTokenStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshOAuthToken {

    private final OAuthTokenStorage oAuthTokenStorage;

    public Optional<JWTokenBo> run(String refreshToken) {
        log.debug("Input parameter -> refreshToken {}", refreshToken);
        Optional<JWTokenBo> result = oAuthTokenStorage.refreshToken(refreshToken);
        log.debug("Output -> {}", result);
        return result;
    }

}
