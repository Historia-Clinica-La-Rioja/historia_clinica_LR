package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FetchUserProfessionalLicensesFromTokenDto {
	private String licenseType;
	private String licenseNumber;
}
