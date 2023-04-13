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
public class CHDocumentNotesSummary {

	private String currentIllness;
	private String physicalExam;
	private String evolution;
	private String clinicalImpression;
	private String otherNote;

}
