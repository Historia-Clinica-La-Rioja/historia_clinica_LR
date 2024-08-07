package net.pladema.provincialreports.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ComplementaryStudiesConsultationDetail {

	private String institution;

	private String serviceRequestDate;

	private String serviceRequestCategory;

	private String orderStatus;

	private String requestType;

	private String requestOrigin;

	private String patientFullName;

	private String patientDocumentType;

	private String patientDocumentNumber;

	private String medicalCoverage;

	private String affiliateNumber;

	private String professionalFullName;

	private String professionalDocumentType;

	private String professionalDocumentNumber;

	private String license;

	private String note;

	private String issueDate;

	private String studyName;

	private String additionalNotes;

	private String associatedProblem;

}
