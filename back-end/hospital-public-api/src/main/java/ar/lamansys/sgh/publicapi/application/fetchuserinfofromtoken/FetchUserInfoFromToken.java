package ar.lamansys.sgh.publicapi.application.fetchuserinfofromtoken;

import ar.lamansys.sgh.publicapi.application.port.PublicUserStorage;
import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FetchUserInfoFromToken {

	private final PublicUserStorage publicUserStorage;

	public FetchUserInfoFromToken(PublicUserStorage publicUserStorage) {
		this.publicUserStorage = publicUserStorage;
	}
	public Optional<PublicUserInfoBo> execute(String token) {
		return publicUserStorage.fetchUserInfoFromToken(token);
	}
}
