package net.pladema.odontologyreport.repository;

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
public class OdontologyProceduresReports {

	private String institution;

	private String professionalName;

	private String professionalId;

	private String professionalRegistrationNumber;

	private String attentionDate;

	private String attentionHour;

	private String patientName;

	private String patientId;

	private String patientSex;

	private String patientGender;

	private String patientSelfPerceivedName;

	private String patientBirthDate;

	private String patientAgeWhenAttended;

	private String patientAge;

	private String patientSocialCoverage;

	private String patientAddress;

	private String patientLocation;

	private String patientPermanentCPO;

	private String patientTemporaryCEO;

	private String reasons;

	private String otherDiagnoses;

	private String otherProcedures;

	private String allergies;

	private String commonMedication;

	private String dentalDiagnoses;

	private String dentalProcedures;

	private String evolution;
}
