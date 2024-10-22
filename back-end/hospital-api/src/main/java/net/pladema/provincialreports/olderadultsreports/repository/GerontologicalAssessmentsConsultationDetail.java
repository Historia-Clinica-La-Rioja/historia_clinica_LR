package net.pladema.provincialreports.olderadultsreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GerontologicalAssessmentsConsultationDetail {

	private String professionalFullName;

	private String professionalIdentificationNumber;

	private String clinicalSpecialty;

	private String patientFullName;

	private String patientIdentificationNumber;

	private String gender;

	private String birthDate;

	private String ageAtAssessmentRecord;

	private String medicalCoverage;

	private String attentionDate;

	private String hour;

	private String assessmentName;

	private String assessmentScore;

	private String assessmentScoreInterpretation;
}
