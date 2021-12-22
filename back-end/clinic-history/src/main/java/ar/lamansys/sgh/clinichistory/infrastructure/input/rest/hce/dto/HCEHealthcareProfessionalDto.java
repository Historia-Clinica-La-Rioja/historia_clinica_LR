package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HCEHealthcareProfessionalDto {

	private Integer id;

	private String licenseNumber;

	private HCEBasicPersonDataDto person;
	
}
