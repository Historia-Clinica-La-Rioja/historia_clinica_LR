package ar.lamansys.sgx.auth.oauth.application;

import ar.lamansys.sgx.auth.oauth.application.ports.OAuthAuthenticationStorage;
import ar.lamansys.sgx.auth.oauth.application.ports.OAuthUserStorage;
import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;
import ar.lamansys.sgx.auth.user.domain.user.service.exceptions.UserStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadUserAuthentication {

    private final OAuthAuthenticationStorage oAuthAuthenticationStorage;
    private final OAuthUserStorage oAuthUserStorage;

    public Optional<Authentication> run(OAuthUserInfoBo userInfo) {
        log.debug("Input parameter -> userInfo {}", userInfo);
        Optional<Authentication> authentication;

        try {
            authentication = oAuthAuthenticationStorage.getAppAuthentication(userInfo.getUsername());
        } catch (UserStorageException e) { // this means that the user has not been created yet
            oAuthUserStorage.registerUser(userInfo.getUsername(), userInfo.getEmail(), null);
            oAuthUserStorage.enableUser(userInfo.getUsername());
            authentication = oAuthAuthenticationStorage.getAppAuthentication(userInfo.getUsername());
        }
        return authentication;
    }

}
