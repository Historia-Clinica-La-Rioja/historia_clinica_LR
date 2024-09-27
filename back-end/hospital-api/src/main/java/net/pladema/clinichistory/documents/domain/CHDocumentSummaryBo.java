package net.pladema.clinichistory.documents.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;

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

	@Nullable
	private Integer sourceTypeId;

	@Nullable
	private Integer requestSourceTypeId;

	@Nullable
	private Integer createdByPersonId;

	public CHDocumentSummaryBo(Long documentId, Integer patientId, LocalDateTime startDate, LocalDateTime endDate, Integer createdByPersonId, String institution, Short documentType, Short sourceTypeId, Short requestSourceTypeId) {
		this.id = documentId;
		this.patientId = patientId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdByPersonId = createdByPersonId;
		this.institution = institution;
		this.typeId = documentType;
		this.sourceTypeId = sourceTypeId.intValue();
		this.requestSourceTypeId = requestSourceTypeId.intValue();
	}

	public LocalDateTime getStartDate() {
		return startDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
	}

	public LocalDateTime getEndDate() {
		if (endDate == null) return getStartDate();
		return endDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
	}
}
