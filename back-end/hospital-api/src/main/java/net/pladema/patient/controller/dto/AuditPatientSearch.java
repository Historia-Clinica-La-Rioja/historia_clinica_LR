package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AuditPatientSearch {

	private Boolean name;
	private Boolean identify;
	private Boolean birthdate;

}
