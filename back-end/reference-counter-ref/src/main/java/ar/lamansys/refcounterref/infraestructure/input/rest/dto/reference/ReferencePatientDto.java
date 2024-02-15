package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferencePatientDto {

	private String phonePrefix;

	private String phoneNumber;

	private String email;

	private String patientFullName;

	private String identificationNumber;

	private String identificationType;

	private Integer patientId;

}
