package ar.lamansys.sgx.auth.jwt.domain.authentication;

import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import lombok.Getter;


@Getter
public class AuthenticationBo {

    private final UserInfoBo user;

    public AuthenticationBo(UserInfoBo user) {
        this.user = user;
    }
}
