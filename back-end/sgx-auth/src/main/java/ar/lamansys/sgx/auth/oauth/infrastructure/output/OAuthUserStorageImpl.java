package ar.lamansys.sgx.auth.oauth.infrastructure.output;

import ar.lamansys.sgx.auth.oauth.application.ports.OAuthUserStorage;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthUserStorageImpl implements OAuthUserStorage {

    private final UserExternalService userExternalService;

    public void registerUser(String username, String email, String password) {
        log.debug("Input parameters -> username {}, email {}", username, email);
        userExternalService.registerUser(username, email, password);
    }

    public void enableUser(String username) {
        log.debug("Input parameters -> username {}", username);
        userExternalService.enableUser(username);
    }
}
