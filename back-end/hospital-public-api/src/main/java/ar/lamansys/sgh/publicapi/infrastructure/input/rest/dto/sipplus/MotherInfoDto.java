package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MotherInfoDto {

	private String name;

	private String lastName;

	private String identificationNumber;

	private String identificationType;

	private String birthDate;

}