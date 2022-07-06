package ar.lamansys.sgx.auth.user.application.fetchuserhastwofactorauthenticationenabled;

import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchUserHasTwoFactorAuthenticationEnabledImpl implements FetchUserHasTwoFactorAuthenticationEnabled {

    private final UserStorage userStorage;

    @Override
    public Boolean run(Integer userId) {
        log.debug("Input parameter -> userId {}", userId);
        return userStorage.userHasTwoFactorAuthenticationEnabled(userId);
    }
}
