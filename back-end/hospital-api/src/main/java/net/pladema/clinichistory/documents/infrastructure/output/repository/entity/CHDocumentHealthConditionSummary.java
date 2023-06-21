package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHDocumentHealthConditionSummary {

	private String problems;
	private String familyRecord;
	private String personalRecord;
	private String procedures;
	private String medicines;
	private String allergies;
	private String vaccines;
	private String riskFactors;
	private String outpatientConsultationReasons;
	private String odontologyProcedure;
	private String odontologyDiagnostic;

}
