package ar.lamansys.sgh.publicapi.userinformation.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FetchUserProfessionalLicensesFromTokenBo {
	private String licenseType;
	private String licenseNumber;
}
