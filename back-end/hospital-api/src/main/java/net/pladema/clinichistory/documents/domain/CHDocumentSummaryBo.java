package net.pladema.clinichistory.documents.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CHDocumentSummaryBo {

	private Long id;

	private Integer patientId;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private ECHEncounterType encounterType;

	private ECHDocumentType documentType;

	private String professional;

	private String institution;

	private String problems;

	private Short typeId;

}
