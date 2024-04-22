package net.pladema.provincialreports.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmergencyConsultationDetail {

	private String institution;

	private String ambulance;

	private String office;

	private String sector;

	private String policeIntervention;

	private String attentionDate;

	private String attentionHour;

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

	private String reasons;

	private String problems;

	private String evolution;

}
