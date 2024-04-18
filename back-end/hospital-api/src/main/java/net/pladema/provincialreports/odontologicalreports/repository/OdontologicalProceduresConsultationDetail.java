package net.pladema.provincialreports.odontologicalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OdontologicalProceduresConsultationDetail {

	private String professionalName;

	private String professionalIdentificationNumber;

	private String professionalLicenseNumber;

	private String attentionDate;

	private String attentionHour;

	private String patientNames;

	private String patientIdentificationNumber;

	private String patientGender;

	private String patientSelfPerceivedGender;

	private String patientSelfPerceivedName;

	private String patientBirthDate;

	private String patientAgeTurn;

	private String patientAgeToday;

	private String patientMedicalCoverage;

	private String patientAddress;

	private String patientLocation;

	private String patientPermanentCPO;

	private String patientTemporaryCEO;

	private String reasons;

	private String otherDiagnoses;

	private String otherProcedures;

	private String allergies;

	private String usualMedication;

	private String dentalDiagnoses;

	private String dentalProcedures;

	private String evolution;

}
