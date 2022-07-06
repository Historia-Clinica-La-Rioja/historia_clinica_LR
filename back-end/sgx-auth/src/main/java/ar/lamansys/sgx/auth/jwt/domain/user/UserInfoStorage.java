package ar.lamansys.sgx.auth.jwt.domain.user;

public interface UserInfoStorage {

	UserInfoBo getUser(String username);

	UserInfoBo getUser(Integer userId);

    void updateLoginDate(String username);

    Boolean fetchUserHasTwoFactorAuthenticationEnabled(Integer userId);

}
