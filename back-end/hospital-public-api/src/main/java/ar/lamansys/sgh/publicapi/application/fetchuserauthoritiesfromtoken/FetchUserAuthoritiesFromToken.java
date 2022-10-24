package ar.lamansys.sgh.publicapi.application.fetchuserauthoritiesfromtoken;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.domain.authorities.PublicAuthorityBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.PublicUserStorage;
import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FetchUserAuthoritiesFromToken {

	private final PublicUserStorage publicUserStorage;

	public FetchUserAuthoritiesFromToken(PublicUserStorage publicUserStorage) {
		this.publicUserStorage = publicUserStorage;
	}
	public List<PublicAuthorityBo> execute(String token) {
		return publicUserStorage.fetchRolesFromToken(token);
	}
}
