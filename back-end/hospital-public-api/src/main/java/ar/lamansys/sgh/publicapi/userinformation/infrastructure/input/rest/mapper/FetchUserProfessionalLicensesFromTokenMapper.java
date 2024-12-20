package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserProfessionalLicensesFromTokenBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.FetchUserProfessionalLicensesFromTokenDto;

public class FetchUserProfessionalLicensesFromTokenMapper {

	public static FetchUserProfessionalLicensesFromTokenDto fromBo(
			FetchUserProfessionalLicensesFromTokenBo fetchUserProfessionalLicensesFromTokenBo){
		return FetchUserProfessionalLicensesFromTokenDto.builder()
				.licenseNumber(fetchUserProfessionalLicensesFromTokenBo.getLicenseNumber())
				.licenseType(fetchUserProfessionalLicensesFromTokenBo.getLicenseType())
				.build();
	}
}
