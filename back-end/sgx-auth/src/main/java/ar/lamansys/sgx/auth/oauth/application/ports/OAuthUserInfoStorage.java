package ar.lamansys.sgx.auth.oauth.application.ports;

import ar.lamansys.sgx.auth.oauth.domain.OAuthUserInfoBo;

import java.util.Optional;

public interface OAuthUserInfoStorage {

    Optional<OAuthUserInfoBo> getUserInfo(String accessToken);

}
