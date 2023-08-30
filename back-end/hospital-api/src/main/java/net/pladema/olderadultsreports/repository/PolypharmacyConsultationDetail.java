package net.pladema.olderadultsreports.repository;

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
public class PolypharmacyConsultationDetail {

	private String patientName;

	private String patientIdType;

	private String patientIdNumber;

	private String patientSex;

	private String patientGender;

	private String patientSelfPerceivedName;

	private String patientBirthDate;

	private String patientAgeWhenAttended;

	private String patientAgeToday;

	private String medication;

	private String snomed;

	private String status;

	private String origin;

	private String lastUpdate;

	private String isChronic;

	private String startDate;

	private String endDate;

	private String suspensionStartDate;

	private String suspensionEndDate;

	private String relatedProblem;

	private String problemType;

	private String problemStatus;

	private String severity;

	private String verification;

	private String wasPrescribed;

	private String hasPrescription;

	private String prescriptionCategory;

	private String prescriptionStatus;

	private String prescriptionType;

	private String medicalInsurance;

	private String affiliateNumber;

	private String institution;

	private String professionalName;

	private String professionalIdNumber;

	private String professionalLicense;

	private String professionalSpeciality;

	private String prescriptionDate;

	private String prescriptionUpdateDate;

}
