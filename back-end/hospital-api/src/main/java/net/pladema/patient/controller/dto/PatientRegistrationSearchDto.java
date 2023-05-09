package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.controller.dto.BMPersonDto;

@Getter
@Setter
@ToString
public class PatientRegistrationSearchDto {

	private BMPersonDto person;

	private Integer idPatient;

	private float ranking;

	private Short patientTypeId;

	private Short auditTypeId;

	private String nameSelfDetermination;

}
