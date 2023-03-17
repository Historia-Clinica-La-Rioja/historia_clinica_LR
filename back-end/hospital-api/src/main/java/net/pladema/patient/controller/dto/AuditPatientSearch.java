package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuditPatientSearch {

	private boolean name;
	private boolean identify;
	private boolean birthdate;

}
