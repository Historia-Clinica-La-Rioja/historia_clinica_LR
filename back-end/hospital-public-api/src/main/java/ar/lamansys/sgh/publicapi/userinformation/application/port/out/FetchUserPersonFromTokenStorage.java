package ar.lamansys.sgh.publicapi.userinformation.application.port.out;

import java.util.Optional;

import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;

public interface FetchUserPersonFromTokenStorage {
	Optional<FetchUserPersonFromTokenBo> getUserPersonFromToken(String token);
}
