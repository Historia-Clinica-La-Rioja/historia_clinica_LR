package net.pladema.emergencycare.triage.controller.dto;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentDownloadDataBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TriageDocumentDto {

	private TriageListDto triage;

	private Long documentId;

	private String fileName;

	public TriageDocumentDto(TriageListDto triage, DocumentDownloadDataBo documentDownloadData) {
		this.triage = triage;
		this.documentId = documentDownloadData.getId();
		this.fileName = documentDownloadData.getFileName();
	}

}
