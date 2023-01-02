package net.pladema.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OutpatientNursing {

	private String institution;

	private String operativeUnit;

	private String patientProvider;

	private String providerDni;

	private String attentionDate;

	private String hour;

	private String consultationNumber;

	private String patientDni;

	private String patientName;

	private String gender;

	private String selfPerceivedGender;

	private String selfPerceivedName;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String ethnicity;

	private String medicalCoverage;

	private String patientAddress;

	private String patientLocation;

	private String educationLevel;

	private String occupation;

	private String vitalSign;

	private String procedures;

	private String evolution;

}