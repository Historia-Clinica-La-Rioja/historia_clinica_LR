package net.pladema.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ConsultationDetailEmergencias {

	private String institution;

	private String ambulance;

	private String office;

	private String sector;

	private String policeIntervention;

	private String attentionDate;

	private String identification;

	private String lastName;

	private String names;

	private String medicalCoverage;

	private String emergencyCareEntrance;

	private String situation;

	private String emergencyCareType;

	private String triageNote;

	private String triageLevel;

	private String dateDischarge;

	private String ambulanceDischarge;

	private String typeDischarge;

	private String patientExit;

}
