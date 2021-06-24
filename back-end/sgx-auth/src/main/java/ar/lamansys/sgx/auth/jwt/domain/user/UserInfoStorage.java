package ar.lamansys.sgx.auth.jwt.domain.user;

public interface UserInfoStorage {

    UserInfoBo getUser(String username);

    void updateLoginDate(String username);
}
