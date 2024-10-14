package ar.lamansys.sgh.publicapi.userinformation.application.port.out;

import ar.lamansys.sgh.publicapi.userinformation.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.userinformation.domain.PublicUserInfoBo;

import java.util.List;
import java.util.Optional;

public interface PublicUserStorage {
	Optional<PublicUserInfoBo> fetchUserInfoFromToken(String token);
	List<PublicAuthorityBo> fetchRolesFromToken(String token);
}
