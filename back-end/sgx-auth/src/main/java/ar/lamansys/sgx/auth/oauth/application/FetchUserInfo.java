package ar.lamansys.sgx.auth.oauth.application;

import ar.lamansys.sgx.auth.oauth.application.ports.OAuthUserInfoStorage;
import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchUserInfo {

    public static final String BEARER = "Bearer ";
    private final OAuthUserInfoStorage oAuthUserInfoStorage;

    public Optional<OAuthUserInfoBo> run(String accessToken) {
        if (!accessToken.startsWith(BEARER))
            accessToken = BEARER + accessToken;
        Optional<OAuthUserInfoBo> result = oAuthUserInfoStorage.getUserInfo(accessToken);
        log.debug("Output -> {}", result);
        return result;
    }

}
