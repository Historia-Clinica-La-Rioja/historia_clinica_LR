package ar.lamansys.sgh.publicapi.userinformation.application.port.out;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserProfessionalLicensesFromTokenBo;

public interface FetchUserProfessionalLicensesFromTokenStorage {
	Optional<List<FetchUserProfessionalLicensesFromTokenBo>> getProfessionalLicensesFromToken(String userToken);
}
