package net.pladema.provincialreports.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DiabeticHypertensionConsultationDetail {

	private String institution;

	private String attentionDate;

	private String lenderLastNames;

	private String lenderNames;

	private String lenderDni;

	private String patientLastNames;

	private String patientNames;

	private String patientDni;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String reasons;

	private String problem;

	private String glycosylatedHemoglobinBloodPressure;

	private String medication;

}
