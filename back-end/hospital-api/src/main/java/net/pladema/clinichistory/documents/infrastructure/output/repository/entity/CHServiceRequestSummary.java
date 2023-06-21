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
public class CHServiceRequestSummary {

	private String serviceRequestDetails;
	private String serviceRequestStudies;
	private String serviceRequestResult;

}
