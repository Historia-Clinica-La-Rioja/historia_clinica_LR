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
public class CHDocumentSummary {

	private String problems;
	private String familyRecord;
	private String personalRecord;
	private String procedures;
	private String medicines;
	private String allergies;
	private String vaccines;
	private String bloodType;
	private String anthropometricData;
	private String epicrisisOtherCircumstances;
	private String epicrisisExternalCause;
	private String epicrisisObstetricEvent;
	private String riskFactors;
	private String pediatricRiskFactors;
	private String outpatientReferences;
	private String serviceRequestCategory;
	private String serviceRequestStudies;
	private String consultationReasons;
	private String odontologyProcedure;
	private String odontologyDiagnostic;
	private String odontologyPieces;
	private String indication;
	private String referenceCounterReference;
	private String counterReferenceClosure;
	private String notes;

}
