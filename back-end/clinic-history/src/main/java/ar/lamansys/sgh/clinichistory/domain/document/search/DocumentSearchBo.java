package ar.lamansys.sgh.clinichistory.domain.document.search;

import ar.lamansys.sgh.clinichistory.domain.document.AuthorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchBo {

    private Long id;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private AuthorBo creator;

    private LocalDateTime createdOn;

    private String documentType;

	private Long initialDocumentId;

	private LocalDateTime editedOn;
    
	private boolean confirmed;

    public DocumentSearchBo(DocumentSearchVo source) {
        this.id = source.getId();
        this.mainDiagnosis = source.getMainDiagnosis();
        this.diagnosis = source.getDiagnosis();
        this.creator = new AuthorBo(source.getCreator().getUserId(), source.getCreator().getFirstName(), source.getCreator().getLastName(), source.getCreator().getNameSelfDetermination());
        this.createdOn = source.getCreatedOn();
        this.documentType = source.getDocumentType();
		this.initialDocumentId = source.getInitDocumentId();
		this.confirmed = source.isFinal();
    }
}
