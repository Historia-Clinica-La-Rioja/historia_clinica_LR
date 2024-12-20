package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserprofessionallicensesfromtoken;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.NoLicensesException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserInformationAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserNotExistsException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.FetchUserProfessionalLicensesFromTokenStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserProfessionalLicensesFromTokenBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchUserProfessionalLicensesFromToken {

	private final FetchUserProfessionalLicensesFromTokenStorage fetchUserProfessionalLicensesFromTokenStorage;
	private final UserInformationPublicApiPermission userInformationPublicApiPermission;

	public List<FetchUserProfessionalLicensesFromTokenBo> run(String userToken) {
		assertUserCanFetchProfessionalLicenses();
		try{
			var info = this.fetchUserProfessionalLicensesFromTokenStorage.getProfessionalLicensesFromToken(userToken);
			if(info.isEmpty()) {
				throw new UserNotExistsException();
			}
			else {
				return info.get();
			}
		}
		catch (NoResultException exc) {
			throw new NoLicensesException();
		}
	}

	private void assertUserCanFetchProfessionalLicenses() {
		if (!userInformationPublicApiPermission.canFetchProfessionalLicenses()) {
			throw new UserInformationAccessDeniedException();
		}
	}
}
