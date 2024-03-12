package net.pladema.electronicjointsignature.documentlist.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicSignatureInvolvedDocumentBo {

	private Long documentId;

	private Short documentTypeId;

	private Integer patientPersonId;

	private Integer responsibleHealthcareProfessionalPersonId;

	private String patientCompleteName;

	private String responsibleProfessionalCompleteName;

	private LocalDateTime documentCreationDate;

	private Short signatureStatusId;

	private LocalDate statusDate;

	private List<String> problems;

	public ElectronicSignatureInvolvedDocumentBo(Long documentId, Short documentTypeId, Integer patientPersonId, Integer responsibleHealthcareProfessionalPersonId,
												 LocalDateTime documentCreationDate, Short signatureStatusId, LocalDate statusDate) {
		this.documentId = documentId;
		this.documentTypeId = documentTypeId;
		this.patientPersonId = patientPersonId;
		this.responsibleHealthcareProfessionalPersonId = responsibleHealthcareProfessionalPersonId;
		this.documentCreationDate = documentCreationDate;
		this.signatureStatusId = signatureStatusId;
		this.statusDate = statusDate;
	}
}
