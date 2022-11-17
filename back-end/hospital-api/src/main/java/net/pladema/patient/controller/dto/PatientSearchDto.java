package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.BMPersonDto;

@Getter
@Setter
@NoArgsConstructor
public class PatientSearchDto {
	
	private BMPersonDto person;
	private float ranking; 
	private Integer idPatient;
	private Boolean activo;
	private String nameSelfDetermination;

}
