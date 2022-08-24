package ar.lamansys.sgh.clinichistory.application.searchDocument;

import ar.lamansys.sgh.clinichistory.domain.document.AuthorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ProcedureReduced;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.DocumentSearchVo;
import lombok.*;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchBo {

    private Long id;

    private DocumentObservationsBo notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private List<ProcedureReduced> procedures;

    private AuthorBo creator;

    private LocalDateTime createdOn;

    private String documentType;

	private Long initialDocumentId;

	private LocalDateTime editedOn;
    
	private boolean confirmed;

    public DocumentSearchBo(DocumentSearchVo source) {
        this.id = source.getId();
        if(source.getNotes() != null)
            this.notes = new DocumentObservationsBo(source.getNotes());
        this.mainDiagnosis = source.getMainDiagnosis();
        this.diagnosis = source.getDiagnosis();
        this.procedures = source.getProcedures();
        this.creator = new AuthorBo(source.getCreator().getUserId(), source.getCreator().getFirstName(), source.getCreator().getLastName(), source.getCreator().getNameSelfDetermination());
        this.createdOn = source.getCreatedOn();
        this.documentType = source.getDocumentType();
		this.initialDocumentId = source.getInitDocumentId();
		this.confirmed = source.isFinal();
    }
}
