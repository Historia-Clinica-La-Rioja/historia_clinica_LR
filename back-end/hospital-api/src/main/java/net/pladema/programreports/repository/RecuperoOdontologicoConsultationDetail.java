package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RecuperoOdontologicoConsultationDetail {

	private String institution;

	private String operativeUnit;

	private String lender;

	private String identificationNumber;

	private String attentionDate;

	private String hour;

	private String patientIdentificationNumber;

	private String patientName;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String medicalCoverage;

	private String address;

	private String location;

	private String cpo;

	private String ceo;

	private String reasons;

	private String procedures;

	private String odontologyProcedures;

	private String problems;

	private String odontologyDiagnosis;

}
