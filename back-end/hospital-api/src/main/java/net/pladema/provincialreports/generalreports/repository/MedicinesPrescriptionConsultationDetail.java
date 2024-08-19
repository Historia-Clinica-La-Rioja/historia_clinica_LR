package net.pladema.provincialreports.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MedicinesPrescriptionConsultationDetail {

	private String institutionName;
	private String prescriptionStatus;
	private String date;
	private String prescriber;
	private String prescriberDNI;
	private String prescriberLicense;
	private String prescriberNationalLicense;
	private String prescriberProvincialLicense;
	private String patient;
	private String patientDNI;
	private String medicalCoverage;
	private String medicine;
	private String relatedDiagnosis;
	private String isChronic;
	private String event;
	private String duration;
	private String frequency;
	private String startDate;
	private String endDate;
	private String suspensionStartDate;
	private String suspensionEndDate;
	private String dosage;
	private String dosePerDay;
	private String dosePerUnit;
	private String observations;

}
