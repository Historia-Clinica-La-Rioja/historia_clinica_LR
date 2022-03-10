package ar.lamansys.sgx.auth.user.domain.user.service;

import ar.lamansys.sgx.auth.user.domain.user.model.OAuthUserBo;

public interface OAuthUserManagementStorage {

    void createUser(OAuthUserBo oAuthUserBo);

    void updateUser(String currentUsername, OAuthUserBo newUserData);

}
