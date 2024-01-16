package ar.lamansys.sgh.publicapi.userinformation.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FetchUserPersonFromTokenBo {
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
