package net.pladema.provincialreports.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RecoveryOdontologyConsultationDetail {

	private String institution;

	private String operativeUnit;

	private String lender;

	private String lenderDni;

	private String attentionDate;

	private String attentionHour;

	private String patientDni;

	private String patientName;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String medicalCoverage;

	private String direction;

	private String neighborhood;

	private String location;

	private String indexCpo;

	private String indexCeo;

	private String reasons;

	private String procedures;

	private String dentalProcedures;

	private String problems;

	private String dentistryDiagnostics;

}
