package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentTypeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SourceTypeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class DigitalSignatureDocumentDto implements Serializable {

	private Long documentId;
	private SourceTypeDto sourceTypeDto;
	private LocalDateTime createdOn;
	private String professionalFullName;
	private List<SnomedConceptDto> snomedConcepts;
	private DocumentTypeDto documentTypeDto;
	private String patientFullName;
}
