package net.pladema.clinichistory.documents.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHDocumentSummaryDto {

	private Long id;

	private String startDate;

	private String endDate;

	private String encounterType;

	private String professional;

	private String institution;

	private String problems;


}
