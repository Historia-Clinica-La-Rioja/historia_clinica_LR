package ar.lamansys.sgx.auth.user.domain.user.service;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;

public interface UserStorage {

    void save(UserBo user);

    void update(UserBo user);

    UserBo getUser(Integer userId);

    UserBo getUser(String username);

	void resetTwoFactorAuthentication(Integer userId);

    Boolean userHasTwoFactorAuthenticationEnabled(Integer userId);
}
