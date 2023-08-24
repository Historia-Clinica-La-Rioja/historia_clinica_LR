package net.pladema.generalreports.repository;

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

	private String date;

	private String category;

	private String order;

	private String typeOfRequest;

	private String origin;

	private String patientName;

	private String documentTypePatient;

	private String documentNumberPatient;

	private String socialWork;

	private String affiliateNumber;

	private String professionalName;

	private String documentTypeProfessional;

	private String documentNumberProfessional;

	private String license;

	private String note;

	private String dateOfIssue;

	private String studyName;

	private String additionalNotes;

	private String associatedProblems;

}
