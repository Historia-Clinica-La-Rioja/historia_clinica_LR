package ar.lamansys.sgh.clinichistory.domain.document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DigitalSignatureDocumentBo {

	private Long documentId;
	private SourceTypeBo sourceTypeBo;
	private LocalDateTime createdOn;
	private String professionalFullName;
	private DocumentTypeBo documentTypeBo;
	private String patientFullName;
	private List<SnomedConceptBo> snomedConceptBo;
	private Integer patientPersonId;
	private Integer professionalPersonId;

	public DigitalSignatureDocumentBo(Long documentId, SourceType sourceType, LocalDateTime createdOn, Short documentTypeId, String documentTypeDescription, Integer patientPersonId, Integer professionalPersonId) {
		this.documentId = documentId;
		this.sourceTypeBo = new SourceTypeBo(sourceType.getId(), sourceType.getDescription());
		this.createdOn = createdOn;
		this.documentTypeBo = new DocumentTypeBo(documentTypeId, documentTypeDescription);
		this.patientPersonId = patientPersonId;
		this.professionalPersonId = professionalPersonId;
	}
}
