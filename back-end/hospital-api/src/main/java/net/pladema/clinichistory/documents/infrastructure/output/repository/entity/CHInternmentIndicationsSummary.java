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
public class CHInternmentIndicationsSummary {

	private String diet;
	private String otherIndication;
	private String pharmaco;
	private String parenteralPlan;

}
