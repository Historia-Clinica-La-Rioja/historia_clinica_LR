package net.pladema.provincialreports.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RecuperoGeneralConsultationDetail {

	private String institution;

	private String clinicalSpecialty;

	private String provider;

	private String providerDni;

	private String attentionDate;

	private String hour;

	private String consultationNumber;

	private String patientDni;

	private String patientName;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String medicalCoverage;

	private String address;

	private String location;

	private String bodyMassIndex;//

	private String reasons;

	private String procedures;

	private String problems;

	private String medication;

	private String evolution;



}
