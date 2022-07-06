package ar.lamansys.sgh.publicapi.application.port;

import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;

import java.util.Optional;

public interface PublicUserStorage {
	Optional<PublicUserInfoBo> fetchUserInfoFromToken(String token);
}
