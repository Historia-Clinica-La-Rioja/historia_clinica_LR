package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MotherIdentificationInfoDto {

	private String identificationType;

	private String identificationNumber;

}
