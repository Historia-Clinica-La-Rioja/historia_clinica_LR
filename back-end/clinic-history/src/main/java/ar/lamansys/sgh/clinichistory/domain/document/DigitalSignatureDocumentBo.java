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

	public DigitalSignatureDocumentBo(Long documentId, SourceType sourceType, LocalDateTime createdOn, String professionalName,
									  String professionalLastname, String professionalOtherLastName, Short documentTypeId, String documentTypeDescription,
									  String patientName, String patientLastname, String patientOtherLastName) {
		this.documentId = documentId;
		this.sourceTypeBo = new SourceTypeBo(sourceType.getId(), sourceType.getDescription());
		this.createdOn = createdOn;
		this.professionalFullName = professionalName + " " + professionalLastname + " " + (professionalOtherLastName != null ? professionalLastname : "");
		this.documentTypeBo = new DocumentTypeBo(documentTypeId, documentTypeDescription);
		this.patientFullName = patientName + " " + patientLastname + " " + (patientOtherLastName != null ? patientOtherLastName : "");
	}
}
