package net.pladema.provincialreports.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecuperoGeneralConsultationDetail {

	private String institution;

	private String operativeUnit;

	private String lender;

	private String lenderDni;

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

	private String reasons;

	private String procedures;

	private String problems;

	private String medication;

	private String evolution;

}
