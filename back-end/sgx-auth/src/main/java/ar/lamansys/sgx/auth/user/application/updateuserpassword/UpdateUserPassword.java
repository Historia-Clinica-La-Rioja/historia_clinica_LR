package ar.lamansys.sgx.auth.user.application.updateuserpassword;

import ar.lamansys.sgx.auth.user.application.updatepassword.UpdatePassword;
import ar.lamansys.sgx.auth.user.domain.user.model.OAuthUserBo;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateUserPassword {

    private final UpdatePassword updatePassword;
    private final OAuthUserManagementStorage oAuthUserManagementStorage;

    @Value("${ws.oauth.enabled:false}")
    private boolean oAuthServiceEnabled;

    public void run(UserBo user, String password) {
        log.debug("Input parameter -> user {}", user);

        if (!oAuthServiceEnabled) {
            updatePassword.execute(user.getUsername(), password);
        }
        else {
            oAuthUserManagementStorage.updateUser(user.getUsername(), new OAuthUserBo(null, password, null, null, null));
        }
    }

}
