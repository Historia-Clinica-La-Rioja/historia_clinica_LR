package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.FetchUserPersonFromTokenDto;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;

public class FetchUserPersonFromTokenMapper {

	public static FetchUserPersonFromTokenDto fromBo(FetchUserPersonFromTokenBo fetchUserPersonFromTokenBo) {
		return FetchUserPersonFromTokenDto.builder()
				.id(fetchUserPersonFromTokenBo.getId())
				.cuil(fetchUserPersonFromTokenBo.getCuil())
				.email(fetchUserPersonFromTokenBo.getEmail())
				.familyName(fetchUserPersonFromTokenBo.getFamilyName())
				.gender(fetchUserPersonFromTokenBo.getGender())
				.givenName(fetchUserPersonFromTokenBo.getGivenName())
				.identificationNumber(fetchUserPersonFromTokenBo.getIdentificationNumber())
				.identificationType(fetchUserPersonFromTokenBo.getIdentificationType())
				.sub(fetchUserPersonFromTokenBo.getSub())
				.build();
	}
}
