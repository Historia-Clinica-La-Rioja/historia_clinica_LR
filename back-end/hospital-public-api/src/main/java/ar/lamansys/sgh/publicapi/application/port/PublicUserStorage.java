package ar.lamansys.sgh.publicapi.application.port;

import ar.lamansys.sgh.publicapi.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;

import java.util.List;
import java.util.Optional;

public interface PublicUserStorage {
	Optional<PublicUserInfoBo> fetchUserInfoFromToken(String token);
	List<PublicAuthorityBo> fetchRolesFromToken(String token);
}
