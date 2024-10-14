package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FetchUserPersonFromTokenDto {
	private Integer id;

	private String sub;

	private String email;

	private String givenName;

	private String familyName;

	private String cuil;

	private String identificationType;

	private String identificationNumber;

	private String gender;
}
