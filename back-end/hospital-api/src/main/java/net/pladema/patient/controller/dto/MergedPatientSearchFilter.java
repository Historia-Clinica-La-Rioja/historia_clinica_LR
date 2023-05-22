package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MergedPatientSearchFilter extends PatientSearchFilter {

	private Integer patientId;

	private Boolean temporary;

	private Boolean permanentNotValidated;

	private Boolean validated;

	private Boolean permanent;

}
