package ar.lamansys.sgh.clinichistory.domain.document;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DocumentFileSummaryBo implements Serializable {

	private Integer sourceId;

	private SourceTypeBo sourceType;

	private DocumentTypeBo documentType;

	private Integer userProfessionalId;

	private LocalDateTime startDate;

	private DocumentFileDataBo document;

	private Integer institutionId;

	public DocumentFileSummaryBo(Integer sourceId, Short sourceTypeId, String sourceTypeDescription,
								 Short documentTypeId, String documentTypeDescription,
								 Integer userProfessionalId, LocalDateTime startDate,
								 Long documentId, String fileName, Short signatureStatusTypeId, Integer institutionId) {
		this.sourceId = sourceId;
		this.sourceType = new SourceTypeBo(sourceTypeId, sourceTypeDescription);
		this.documentType = new DocumentTypeBo(documentTypeId, documentTypeDescription);
		this.userProfessionalId = userProfessionalId;
		this.startDate = startDate;
		this.document = new DocumentFileDataBo(documentId, fileName, signatureStatusTypeId);
		this.institutionId = institutionId;
	}
}
