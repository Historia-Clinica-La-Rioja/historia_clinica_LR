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

	private String lastNames;

	private String names;

	private String medicalCoverage;

	private String typeOfEntry;

	private String situation;

	private String emergencyCareType;

	private String triageNotes;

	private String triageLevel;

	private String dischargeDate;

	private String dischargeAmbulance;

	private String dischargeType;

	private String patientExit;

	private String reasons;

	private String problems;

	private String evolution;

}
