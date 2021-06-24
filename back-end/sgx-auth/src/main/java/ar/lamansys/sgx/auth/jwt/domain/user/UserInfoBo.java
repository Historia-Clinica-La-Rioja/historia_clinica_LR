package ar.lamansys.sgx.auth.jwt.domain.user;

import lombok.Getter;


@Getter
public class UserInfoBo {

    private final Integer id;

    private final String username;

    private final boolean enable;

    private final String password;

    public UserInfoBo(Integer id, String username, boolean enable, String password) {
        this.id = id;
        this.username = username;
        this.enable = enable;
        this.password = password;
    }
}
