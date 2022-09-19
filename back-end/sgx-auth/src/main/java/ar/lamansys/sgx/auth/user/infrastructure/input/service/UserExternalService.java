package ar.lamansys.sgx.auth.user.infrastructure.input.service;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;

import java.util.Optional;

public interface UserExternalService {

    Optional<UserInfoDto> getUser(String username);

    Optional<UserInfoDto> getUser(Integer userId);

    void registerUser(String username, String email, String password);

    void enableUser(String username);

    void updatePassword(String username, String password);

    void updateLoginDate(String username);

    void disableUser(String username);

    String createTokenPasswordReset(Integer userId);

    Integer getUserIdByToken(String token);

	void resetTwoFactorAuthentication(Integer userId);

    Boolean fetchUserHasTwoFactorAuthenticationEnabled(Integer userId);
}
