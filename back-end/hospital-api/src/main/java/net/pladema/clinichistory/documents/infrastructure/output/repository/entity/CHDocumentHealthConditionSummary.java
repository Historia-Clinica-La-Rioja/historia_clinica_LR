package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHDocumentHealthConditionSummary {

	private String problems;
	private String observations;
	private String procedures;
	private String medicines;
	private String allergies;
	private String riskFactors;
	private String observationLab;
	private String diagnosticReport;
	private String outpatientConsultationReasons;
	private String odontologyProcedure;
	private String serviceRequest;
	private String odontologyDiagnostic;

}
