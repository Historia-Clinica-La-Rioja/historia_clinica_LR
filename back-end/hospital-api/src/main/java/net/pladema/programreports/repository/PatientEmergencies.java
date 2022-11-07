package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PatientEmergencies {

	private String institution;

	private String ambulance;

	private String office;

	private String sector;

	private String policeIntervention;

	private String attentionDate;

	private String attentionHour;

	private String professionalRegistering;

	private String professionalAttention;

	private String identification;

	private String lastName;

	private String names;

	private String gender;

	private String selfPerceivedGender;

	private String selfPerceivedName;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String ethnicity;

	private String addressPatient;

	private String locationPatient;

	private String medicalCoverage;

	private String emergencyCareEntrance;

	private String statePatient;

	private String attentionType;

	private String triageNote;

	private String triageLevel;

	private String dateDischarge;

	private String ambulanceDischarge;

	private String typeDischarge;

	private String patientExit;

}
